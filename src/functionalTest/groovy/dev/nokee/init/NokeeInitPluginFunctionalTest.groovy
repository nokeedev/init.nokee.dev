package dev.nokee.init

import dev.gradleplugins.integtests.fixtures.AbstractGradleSpecification
import dev.gradleplugins.runnerkit.GradleExecutor
import dev.gradleplugins.runnerkit.GradleRunner
import dev.gradleplugins.runnerkit.GradleWrapperFixture
import spock.lang.Ignore

import java.nio.file.Files
import java.nio.file.StandardCopyOption

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
        executer = executer./*requireIsolatedDaemons().*/withGradleUserHomeDirectory(testDirectory)
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

    // TODO: Move to initscript testing
    def "well-behaved plugin with dependency verification enabled"() {
        given:
        def initScript = file('nokee.init.gradle')
        writeInitScript(initScript)
        executer = executer.usingInitScript(initScript).requireOwnGradleUserHomeDirectory()
        file('gradle/verification-metadata.xml') << '''<?xml version="1.0" encoding="UTF-8"?>
<verification-metadata>
   <configuration>
      <verify-metadata>false</verify-metadata>
      <verify-signatures>true</verify-signatures>
    </configuration>
</verification-metadata>
'''
        file('gradle.properties') << '''org.gradle.dependency.verification=strict
'''

        expect:
        succeeds('tasks')
    }

    void writeInitScript(File initscript) {
        Files.copy(NokeeInitPluginFunctionalTest.class.getResourceAsStream("/dev/nokee/init/internal/wrapper/nokee.init.gradle"), initscript.toPath(), StandardCopyOption.REPLACE_EXISTING)
    }
}
