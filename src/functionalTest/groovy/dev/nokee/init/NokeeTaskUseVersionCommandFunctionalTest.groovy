package dev.nokee.init

import dev.gradleplugins.integtests.fixtures.AbstractGradleSpecification

import static dev.nokee.init.fixtures.GradleRunnerUtils.configurePluginClasspathAsInitScriptDependencies

class NokeeTaskUseVersionCommandFunctionalTest extends AbstractGradleSpecification {
	def setup() {
		def initScriptFile = file('init.gradle')
		initScriptFile << configurePluginClasspathAsInitScriptDependencies() << '''
			apply plugin: dev.nokee.init.NokeeInitPlugin
		'''
		executer = executer.usingInitScript(initScriptFile)
	}

	def "can use nokee version without prior Nokee version configuration"() {
		expect:
		succeeds('nokee', '--use-version=0.4.0')
		def result = succeeds('nokee', '--show-version')
		result.output.contains("Build ':' using Nokee version '0.4.0' (from cache file).")
	}

	def "uses cli flag version instead of currently provided version"() {
		expect:
		succeeds('-Dnokee-version=0.3.0', 'nokee', '--use-version=0.4.0')
		def result = succeeds('nokee', '--show-version')
		result.output.contains("Build ':' using Nokee version '0.4.0' (from cache file).")
	}
}
