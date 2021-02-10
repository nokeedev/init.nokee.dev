package dev.nokee.init

import dev.gradleplugins.integtests.fixtures.AbstractGradleSpecification
import spock.lang.Unroll

import static dev.nokee.init.fixtures.GradleRunnerUtils.configurePluginClasspathAsInitScriptDependencies

class DevNokeeResolutionFunctionalTest extends AbstractGradleSpecification {
	def setup() {
		def initScriptFile = file('init.gradle')
		initScriptFile << configurePluginClasspathAsInitScriptDependencies() << '''
			apply plugin: dev.nokee.init.NokeeInitPlugin
		'''
		executer = executer.usingInitScript(initScriptFile)
	}

	@Unroll
	def "can resolve project plugins with provided version"(version) {
		buildFile << '''
			plugins {
				id 'dev.nokee.jni-library'
			}
		'''

		expect:
		succeeds('help', "-Dnokee-version=${version}")

		where:
		version << ['0.5.0-430640a7', '0.4.0']
	}

	@Unroll
	def "can resolve project plugins with version"(version) {
		buildFile << """
			plugins {
				id 'dev.nokee.jni-library' version '${version}'
			}
		"""

		expect:
		succeeds('help')

		where:
		version << ['0.5.0-430640a7', '0.4.0']
	}

	@Unroll
	def "can resolve settings plugins with provided version"(version) {
		settingsFile << """
			plugins {
				id 'dev.nokee.cmake-build-adapter'
			}
		"""

		expect:
		def result = fails('help', "-Dnokee-version=${version}") // because of bug in cmake plugin
		result.output.contains("CMake Error")

		where:
		version << ['0.5.0-430640a7'] // There is no released settings plugin, yet
	}

	@Unroll
	def "can resolve settings plugins with version"(version) {
		settingsFile << """
			plugins {
				id 'dev.nokee.cmake-build-adapter' version '${version}'
			}
		"""

		expect:
		def result = fails('help', '-i') // because of bug in cmake plugin
		result.output.contains("CMake Error")

		where:
		version << ['0.5.0-430640a7'] // There is no released settings plugin, yet
	}
}
