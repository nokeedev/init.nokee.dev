package dev.nokee.init

import dev.gradleplugins.integtests.fixtures.AbstractGradleSpecification

class NokeeInitPluginFunctionalTest extends AbstractGradleSpecification {
    def "can use init script via command line"() {
        given:
        def initScript = file('nokee.init.gradle')
        initScript << configurePluginClasspathAsBuildScriptDependencies().replace('buildscript', 'initscript') << '''
            apply plugin: dev.nokee.init.NokeeInitPlugin
        '''
        usingInitScript(initScript)

        and:
        file('.gradle/use-nokee-version.txt') << '0.4.0'

        when:
        def result = succeeds('help')

        then:
        result.assertOutputContains("Build ':' use Nokee version '0.4.0'.")
    }

    def "can use init script inside Gradle user home"() {
        given:
        executer = executer.requireIsolatedDaemons().withGradleUserHomeDirectory(testDirectory)
        def initScript = file('init.d/nokee.init.gradle')
        initScript << configurePluginClasspathAsBuildScriptDependencies().replace('buildscript', 'initscript') << '''
            apply plugin: dev.nokee.init.NokeeInitPlugin
        '''

        and:
        file('.gradle/use-nokee-version.txt') << '0.4.0'

        when:
        def result = succeeds('help')

        then:
        result.assertOutputContains("Build ':' use Nokee version '0.4.0'.")
    }

    def "can use nokee version of host build from included build"() {
        given:
        settingsFile << '''
            includeBuild 'child'
        '''
        file('child', settingsFileName).createFile()

        and:
        def initScript = file('nokee.init.gradle')
        initScript << configurePluginClasspathAsBuildScriptDependencies().replace('buildscript', 'initscript') << '''
            apply plugin: dev.nokee.init.NokeeInitPlugin
        '''
        usingInitScript(initScript)

        and:
        file('.gradle/use-nokee-version.txt') << '0.4.0'

        when:
        def result = succeeds('help')

        then:
        result.assertOutputContains("Build ':' use Nokee version '0.4.0'.")
        result.assertOutputContains("Build ':child' use Nokee version '0.4.0'.")
    }

    def "shows help if nokee task executes without options"() {
        def initScript = file('nokee.init.gradle')
        initScript << configurePluginClasspathAsBuildScriptDependencies().replace('buildscript', 'initscript') << '''
            apply plugin: dev.nokee.init.NokeeInitPlugin
        '''
        usingInitScript(initScript)

        when:
        def result = succeeds('nokee')

        then:
        result.assertOutputContains('USAGE: gradlew nokee [option...]')
    }

    def "shows version if nokee task executes with --show-version flag"() {
        given:
        def initScript = file('nokee.init.gradle')
        initScript << configurePluginClasspathAsBuildScriptDependencies().replace('buildscript', 'initscript') << '''
            apply plugin: dev.nokee.init.NokeeInitPlugin
        '''
        usingInitScript(initScript)

        and:
        file('.gradle/use-nokee-version.txt') << '0.4.0'

        when:
        def result = succeeds('nokee', '--show-version')

        then:
        result.assertOutputContains("Build ':' use Nokee version '0.4.0'.")
        result.assertOutputContains('Using Nokee 0.4.0')
    }

    def "shows help if nokee task executes with --show-help flag"() {
        def initScript = file('nokee.init.gradle')
        initScript << configurePluginClasspathAsBuildScriptDependencies().replace('buildscript', 'initscript') << '''
            apply plugin: dev.nokee.init.NokeeInitPlugin
        '''
        usingInitScript(initScript)

        when:
        def result = succeeds('nokee', '--show-help')

        then:
        result.assertOutputContains('USAGE: gradlew nokee [option...]')
    }

    def "can execute build init"() {
        given:
        executer = executer.ignoresMissingSettingsFile()
        def initScript = file('nokee.init.gradle')
        initScript << configurePluginClasspathAsBuildScriptDependencies().replace('buildscript', 'initscript') << '''
            apply plugin: dev.nokee.init.NokeeInitPlugin
        '''
        usingInitScript(initScript)

        expect:
        succeeds('init', '--type', 'nokee-cpp-application', '--dsl', 'groovy')
    }
}
