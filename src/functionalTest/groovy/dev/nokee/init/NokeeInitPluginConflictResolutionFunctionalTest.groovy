package dev.nokee.init

import dev.gradleplugins.integtests.fixtures.AbstractGradleSpecification

import static dev.nokee.init.fixtures.GradleRunnerUtils.configurePluginClasspathAsInitScriptDependencies

class NokeeInitPluginConflictResolutionFunctionalTest extends AbstractGradleSpecification {
	def setup() {
		executer = executer.withGradleUserHomeDirectory(file('user-home'))
	}

	private File writePreviousNokeeInitScript(File initScriptFile) {
		initScriptFile << '''
			initscript {
				repositories {
					maven { url = 'https://repo.nokeedev.net/release' }
				}
				dependencies {
					classpath 'dev.nokee.init:init.nokee.dev:0.7.0'
				}
        	}
        	apply plugin: dev.nokee.init.NokeeInitPlugin
		'''
		return initScriptFile
	}

	private File writeCurrentNokeeInitScript(File initScriptFile) {
		initScriptFile << configurePluginClasspathAsInitScriptDependencies() << '''
        	apply plugin: dev.nokee.init.NokeeInitPlugin
		'''
		return initScriptFile
	}

	def "disable plugin before build listener executes"() {
		writePreviousNokeeInitScript(file('user-home/init.d/nokee.init.gradle'))
		executer = executer.usingInitScript(writeCurrentNokeeInitScript(file('init.gradle')))

		expect:
		def result = succeeds('nokee')
		result.output.contains("")
	}

	def "disable plugin right away when duplication is detected during application"() {
		writeCurrentNokeeInitScript(file('user-home/init.d/nokee.init.gradle'))
		executer = executer.usingInitScript(writePreviousNokeeInitScript(file('init.gradle')))

		expect:
		def result = succeeds('nokee')
		result.output.contains("WARNING: Another Nokee init plugin is already loaded, disabling duplicate plugin loading.")
	}

	def "does not disable any plugin when no class loader duplication"() {
		writeCurrentNokeeInitScript(file('user-home/init.d/nokee.init.gradle'))
		executer = executer.usingInitScript(writeCurrentNokeeInitScript(file('init.gradle')))

		expect:
		def result = succeeds('nokee')
		!result.output.contains("WARNING")
	}
}
