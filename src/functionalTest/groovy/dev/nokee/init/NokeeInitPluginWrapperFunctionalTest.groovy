package dev.nokee.init

import dev.gradleplugins.integtests.fixtures.AbstractGradleSpecification
import dev.gradleplugins.test.fixtures.file.TestFile
import org.gradle.internal.os.OperatingSystem
import org.gradle.util.TextUtil
import spock.lang.Unroll

class NokeeInitPluginWrapperFunctionalTest extends AbstractGradleSpecification {
    def "does not include nokee init script inside project if nokee configuration absent"() {
        given:
        def initScript = file('nokee.init.gradle')
        initScript << configurePluginClasspathAsBuildScriptDependencies().replace('buildscript', 'initscript') << '''
            apply plugin: dev.nokee.init.NokeeInitPlugin
        '''
        usingInitScript(initScript)

        when:
        succeeds('wrapper')

        then:
        !wrapperProperties.containsKey('useNokeeVersion')
        file('gradle').assertHasDescendants('wrapper/gradle-wrapper.properties', 'wrapper/gradle-wrapper.jar')
        !passesNokeeInitScriptFromWrapperScripts(testDirectory)
        !passesNokeeVersionFromWrapperScripts(testDirectory, '0.4.0')
    }

    @Unroll
    def "can include nokee init script inside project via #displayName property"(displayName, flag) {
        given:
        def initScript = file('nokee.init.gradle')
        initScript << configurePluginClasspathAsBuildScriptDependencies().replace('buildscript', 'initscript') << '''
            apply plugin: dev.nokee.init.NokeeInitPlugin
        '''
        usingInitScript(initScript)

        when:
        succeeds('wrapper', "${flag}=0.4.0")

        then:
        wrapperProperties.get('useNokeeVersion') == '0.4.0'
        file('gradle').assertHasDescendants('nokee.init.gradle', 'wrapper/gradle-wrapper.properties', 'wrapper/gradle-wrapper.jar')
        assertNokeeInitScript(file('gradle/nokee.init.gradle'))
        passesNokeeInitScriptFromWrapperScripts(testDirectory)
        passesNokeeVersionFromWrapperScripts(testDirectory, '0.4.0')

        where:
        displayName | flag
        'Gradle'    | '-Puse-nokee-version'
        'System'    | '-Duse-nokee-version'
    }

    def "can include nokee init script inside project via wrapper task configuration"() {
        given:
        def initScript = file('nokee.init.gradle')
        initScript << configurePluginClasspathAsBuildScriptDependencies().replace('buildscript', 'initscript') << '''
            apply plugin: dev.nokee.init.NokeeInitPlugin
        '''
        usingInitScript(initScript)

        and:
        buildFile << '''
            tasks.named('wrapper', Wrapper) {
                nokeeVersion.set '0.5.0'
            }
        '''

        when:
        succeeds('wrapper')

        then:
        wrapperProperties.get('useNokeeVersion') == '0.5.0'
        file('gradle').assertHasDescendants('nokee.init.gradle', 'wrapper/gradle-wrapper.properties', 'wrapper/gradle-wrapper.jar')
        assertNokeeInitScript(file('gradle/nokee.init.gradle'))
        passesNokeeInitScriptFromWrapperScripts(testDirectory)
        passesNokeeVersionFromWrapperScripts(testDirectory, '0.5.0')
    }

    @Unroll
    def "can override nokee version via #displayName property"(displayName, flag) {
        given:
        def initScript = file('nokee.init.gradle')
        initScript << configurePluginClasspathAsBuildScriptDependencies().replace('buildscript', 'initscript') << '''
            apply plugin: dev.nokee.init.NokeeInitPlugin
        '''
        usingInitScript(initScript)

        and:
        buildFile << '''
            tasks.named('wrapper', Wrapper) {
                nokeeVersion.set '0.5.0'
            }
        '''

        when:
        succeeds('wrapper', "${flag}=0.4.0")

        then:
        wrapperProperties.get('useNokeeVersion') == '0.4.0'
        file('gradle').assertHasDescendants('nokee.init.gradle', 'wrapper/gradle-wrapper.properties', 'wrapper/gradle-wrapper.jar')
        assertNokeeInitScript(file('gradle/nokee.init.gradle'))
        passesNokeeInitScriptFromWrapperScripts(testDirectory)
        passesNokeeVersionFromWrapperScripts(testDirectory, '0.4.0')

        where:
        displayName  | flag
        'Gradle'    | '-Puse-nokee-version'
        'System'    | '-Duse-nokee-version'
    }

    def "can relocate wrapper configuration"() {
        given:
        def initScript = file('nokee.init.gradle')
        initScript << configurePluginClasspathAsBuildScriptDependencies().replace('buildscript', 'initscript') << '''
            apply plugin: dev.nokee.init.NokeeInitPlugin
        '''
        usingInitScript(initScript)

        and:
        buildFile << '''
            tasks.named('wrapper', Wrapper) {
                scriptFile = file('dummy/gradlew')
                jarFile = file('dummy/wrapper.jar')
                nokeeInitScriptFile.set file('dummy/init.gradle')
                nokeeVersion.set '0.4.0'
            }
        '''

        when:
        succeeds('wrapper')

        then:
        getWrapperProperties('dummy/wrapper.properties').get('useNokeeVersion') == '0.4.0'
        file('dummy').assertHasDescendants('init.gradle', 'wrapper.properties', 'wrapper.jar', 'gradlew', 'gradlew.bat')
        assertNokeeInitScript(file('dummy/init.gradle'))
        passesNokeeInitScriptFromWrapperScripts(file('dummy'), 'init.gradle')
        passesNokeeVersionFromWrapperScripts(file('dummy'), '0.4.0')
    }

    def "can invoke generated wrapper if relocated"() {
        given:
        def initScript = file('nokee.init.gradle')
        initScript << configurePluginClasspathAsBuildScriptDependencies().replace('buildscript', 'initscript') << '''
            apply plugin: dev.nokee.init.NokeeInitPlugin
        '''
        usingInitScript(initScript)

        and:
        buildFile << '''
            tasks.named('wrapper', Wrapper) {
                scriptFile = file('dummy/gradlew')
                jarFile = file('dummy/wrapper.jar')
                nokeeInitScriptFile.set file('dummy/init.gradle')
                nokeeVersion.set '0.4.0'
            }
        '''

        and:
        succeeds('wrapper')

        when:
        def process = [file(OperatingSystem.current().getScriptName('dummy/gradlew')), 'nokee', '--show-version'].execute(null, testDirectory)

        then:
        process.waitFor() == 0
        def out = process.in.text
        out.contains("Build ':' use Nokee version '0.4.0'.")
        out.contains('Using Nokee 0.4.0')
    }

    def "can invoke generated wrapper"() {
        given:
        def initScript = file('nokee.init.gradle')
        initScript << configurePluginClasspathAsBuildScriptDependencies().replace('buildscript', 'initscript') << '''
            apply plugin: dev.nokee.init.NokeeInitPlugin
        '''
        usingInitScript(initScript)

        and:
        succeeds('wrapper', '-Duse-nokee-version=0.4.0')

        when:
        def process = [file(OperatingSystem.current().getScriptName('gradlew')), 'nokee', '--show-version'].execute(null, testDirectory)

        then:
        process.waitFor() == 0
        def out = process.in.text
        out.contains("Build ':' use Nokee version '0.4.0'.")
        out.contains('Using Nokee 0.4.0')
    }

    def "can read nokee version from wrapper properties"() {
        given:
        def initScript = file('nokee.init.gradle')
        initScript << configurePluginClasspathAsBuildScriptDependencies().replace('buildscript', 'initscript') << '''
            apply plugin: dev.nokee.init.NokeeInitPlugin
        '''
        usingInitScript(initScript)

        and:
        succeeds('wrapper', '-Duse-nokee-version=0.4.0')

        when:
        def result = succeeds('nokee', '--show-version')

        then:
        result.assertOutputContains("Build ':' use Nokee version '0.4.0'.")
        result.assertOutputContains('Using Nokee 0.4.0')
    }

    def "prefers use-nokee-version file over useNokeeVersionFromWrapper System property"() {
        given:
        def initScript = file('nokee.init.gradle')
        initScript << configurePluginClasspathAsBuildScriptDependencies().replace('buildscript', 'initscript') << '''
            apply plugin: dev.nokee.init.NokeeInitPlugin
        '''
        usingInitScript(initScript)

        and:
        succeeds('wrapper', '-Duse-nokee-version=0.2.0')

        and:
        file('.gradle/use-nokee-version.txt') << '0.3.0'

        when:
        def process = [file(OperatingSystem.current().getScriptName('gradlew')), 'nokee', '--show-version'].execute(null, testDirectory)

        then:
        process.waitFor() == 0
        def out = process.in.text
        out.contains("Build ':' use Nokee version '0.3.0'.")
        out.contains('Using Nokee 0.3.0')
    }

    def "prefers USE_NOKEE_VERSION environment variable over use-nokee-version file and useNokeeVersionFromWrapper System property"() {
        given:
        def initScript = file('nokee.init.gradle')
        initScript << configurePluginClasspathAsBuildScriptDependencies().replace('buildscript', 'initscript') << '''
            apply plugin: dev.nokee.init.NokeeInitPlugin
        '''
        usingInitScript(initScript)

        and:
        succeeds('wrapper', '-Duse-nokee-version=0.2.0')

        and:
        file('.gradle/use-nokee-version.txt') << '0.3.0'

        when:
        def process = [file(OperatingSystem.current().getScriptName('gradlew')), 'nokee', '--show-version'].execute(environmentVariable('USE_NOKEE_VERSION', '0.4.0'), testDirectory)

        then:
        process.waitFor() == 0
        def out = process.in.text
        out.contains("Build ':' use Nokee version '0.4.0'.")
        out.contains('Using Nokee 0.4.0')
    }

    def "prefers use-nokee-version System property over use-nokee-version file, useNokeeVersionFromWrapper System property and USE_NOKEE_VERSION environment variable"() {
        given:
        def initScript = file('nokee.init.gradle')
        initScript << configurePluginClasspathAsBuildScriptDependencies().replace('buildscript', 'initscript') << '''
            apply plugin: dev.nokee.init.NokeeInitPlugin
        '''
        usingInitScript(initScript)

        and:
        succeeds('wrapper', '-Duse-nokee-version=0.2.0')

        and:
        file('.gradle/use-nokee-version.txt') << '0.3.0'

        when:
        def process = [file(OperatingSystem.current().getScriptName('gradlew')), 'nokee', '--show-version', '-Duse-nokee-version=0.4.0'].execute(environmentVariable('USE_NOKEE_VERSION', '0.5.0'), testDirectory)

        then:
        process.waitFor() == 0
        def out = process.in.text
        out.contains("Build ':' use Nokee version '0.4.0'.")
        out.contains('Using Nokee 0.4.0')
    }

    private Properties getWrapperProperties(String pathToWrapperProperties = 'gradle/wrapper/gradle-wrapper.properties') {
        def result = new Properties()
        file(pathToWrapperProperties).withInputStream { inStream ->
            result.load(inStream)
        }
        return result
    }

    private static void assertNokeeInitScript(File initScriptFile) {
        assert initScriptFile.exists()
        assert TextUtil.normaliseLineSeparators(initScriptFile.text) == '''initscript {
            |    repositories {
            |        maven { url = 'https://dl.bintray.com/nokeedev/distributions-initialization' }
            |        mavenLocal {
            |            content {
            |                it.includeModule('dev.nokee.init', 'init.nokee.dev')
            |            }
            |        }
            |    }
            |    dependencies {
            |        classpath 'dev.nokee.init:init.nokee.dev:latest.release\'
            |    }
            |}
            |apply plugin: dev.nokee.init.NokeeInitPlugin
            |'''.stripMargin()
    }

    private static boolean passesNokeeInitScriptFromWrapperScripts(TestFile wrapperBaseDirectory, String pathToInitScript = 'gradle/nokee.init.gradle') {
        return wrapperBaseDirectory.file('gradlew').assertIsFile().text.contains("--init-script \"\\\"\$APP_HOME/${pathToInitScript}\\\"\"") && wrapperBaseDirectory.file('gradlew.bat').assertIsFile().text.contains("--init-script \"%APP_HOME%\\${pathToInitScript.replace('/', '\\')}\"")
    }

    private static boolean passesNokeeVersionFromWrapperScripts(TestFile wrapperBaseDirectory, String nokeeVersion) {
        return wrapperBaseDirectory.file('gradlew').assertIsFile().text.contains("-DuseNokeeVersionFromWrapper=${nokeeVersion}") && wrapperBaseDirectory.file('gradlew.bat').assertIsFile().text.contains("-DuseNokeeVersionFromWrapper=${nokeeVersion}")
    }

    private static List<String> environmentVariable(String key, String value) {
        def result = []
        def env = [:]
        env.putAll(System.getenv())
        env.put(key, value)
        env.each {k, v ->
            result << "${k}=${v}"
        }
        return result
    }
}
