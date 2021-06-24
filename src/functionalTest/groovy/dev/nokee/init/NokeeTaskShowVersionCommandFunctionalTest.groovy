package dev.nokee.init

import dev.gradleplugins.integtests.fixtures.AbstractGradleSpecification

import static dev.nokee.init.fixtures.GradleRunnerUtils.configurePluginClasspathAsInitScriptDependencies

class NokeeTaskShowVersionCommandFunctionalTest extends AbstractGradleSpecification {
	def setup() {
		def initScriptFile = file('init.gradle')
		initScriptFile << configurePluginClasspathAsInitScriptDependencies() << '''
			apply plugin: dev.nokee.init.NokeeInitPlugin
		'''
		executer = executer.usingInitScript(initScriptFile).withArgument('-Dnokee-version=0.3.0')
	}

	def "shows version provided by buildSrc included build"() {
		file('buildSrc', buildFileName) << '''
			repositories {
				mavenCentral()
				maven { url = 'https://repo.nokeedev.net/release' }
			}

			dependencies {
				implementation platform("dev.nokee:nokee-gradle-plugins:0.4.0")
			}
		'''

		expect:
		def result = succeeds('nokee', '--show-version')
		result.output.contains("Build ':' using Nokee version '0.4.0' (from build classpath).")
	}
}
