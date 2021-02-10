package dev.nokee.init

import dev.gradleplugins.integtests.fixtures.AbstractGradleSpecification

import static dev.nokee.init.fixtures.GradleRunnerUtils.configurePluginClasspathAsInitScriptDependencies

class NokeeTaskShowVersionCommandIncludedBuildFunctionalTest extends AbstractGradleSpecification {
	def setup() {
		def initScriptFile = file('init.gradle')
		initScriptFile << configurePluginClasspathAsInitScriptDependencies() << '''
			apply plugin: dev.nokee.init.NokeeInitPlugin
		'''
		executer = executer.usingInitScript(initScriptFile).withArgument('-Dnokee-version=0.4.0')

		settingsFile << '''
			includeBuild 'nested'
		'''
		file('nested', settingsFileName).createFile()
	}

	def "calls included build nokee task when using System property CLI flag"() {
		expect:
		def result = succeeds('nokee', '-Dshow-version')
		result.assertTasksExecutedAndNotSkipped(':nokee', ':nested:nokee')
	}

	def "does not call included build nokee task when using Gradle CLI flag support"() {
		// \-> because Gradle CLI flag support are not propagated into included build
		//     causing the :nested:nokee task to just print help message, not a good UX.
		expect:
		def result = succeeds('nokee', '--show-version')
		result.assertTasksExecutedAndNotSkipped(':nokee')
	}
}
