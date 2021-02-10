package dev.nokee.init

import dev.gradleplugins.integtests.fixtures.AbstractGradleSpecification

import static dev.nokee.init.fixtures.GradleRunnerUtils.configurePluginClasspathAsInitScriptDependencies

class DevGradlePluginsResolutionFunctionalTest extends AbstractGradleSpecification {
	def setup() {
		def initScriptFile = file('init.gradle')
		initScriptFile << configurePluginClasspathAsInitScriptDependencies() << '''
			apply plugin: dev.nokee.init.NokeeInitPlugin
		'''
		executer = executer.usingInitScript(initScriptFile)
	}

	def "can resolve project project plugins"() {
		buildFile << '''
			plugins {
				id 'dev.gradleplugins.java-gradle-plugin' version '1.1.32'
			}
		'''

		expect:
		succeeds('help')
	}

	def "can resolve settings plugins"() {
		settingsFile << """
			plugins {
				id 'dev.gradleplugins.gradle-plugin-development' version '1.1.32'
			}
		"""

		expect:
		succeeds('help')
	}
}
