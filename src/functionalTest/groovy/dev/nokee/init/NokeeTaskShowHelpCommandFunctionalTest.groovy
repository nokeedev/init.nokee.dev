package dev.nokee.init

import dev.gradleplugins.integtests.fixtures.AbstractGradleSpecification

import static dev.nokee.init.fixtures.GradleRunnerUtils.configurePluginClasspathAsInitScriptDependencies

class NokeeTaskShowHelpCommandFunctionalTest extends AbstractGradleSpecification {
	def setup() {
		def initScriptFile = file('init.gradle')
		initScriptFile << configurePluginClasspathAsInitScriptDependencies() << '''
			apply plugin: dev.nokee.init.NokeeInitPlugin
		'''
		executer = executer.usingInitScript(initScriptFile)
	}

	def "shows help when no CLI flags used"() {
		expect:
		def result = succeeds('nokee')
		result.output.contains('''USAGE: gradlew nokee [option...]
			|
			|--show-help\t\tShows help message.
			|--show-version\t\tPrint version info.
			|--use-version\t\tSpecifies the nokee version to use in this project.
			|'''.stripMargin())
	}

	def "shows help for corresponding flag"() {
		expect:
		def result = succeeds('nokee', '--show-help')
		result.output.contains('''USAGE: gradlew nokee [option...]
			|
			|--show-help\t\tShows help message.
			|--show-version\t\tPrint version info.
			|--use-version\t\tSpecifies the nokee version to use in this project.
			|'''.stripMargin())
	}
}
