package dev.nokee.init

import dev.gradleplugins.integtests.fixtures.AbstractGradleSpecification

import static dev.nokee.init.fixtures.GradleRunnerUtils.configurePluginClasspathAsInitScriptDependencies

class GradleWrapperNokeeVersionOverrideFunctionalTest extends AbstractGradleSpecification {
	def setup() {
		def initScriptFile = file('init.gradle')
		initScriptFile << configurePluginClasspathAsInitScriptDependencies() << '''
			apply plugin: dev.nokee.init.NokeeInitPlugin
		'''
		executer = executer.usingInitScript(initScriptFile)

		buildFile << '''
			tasks.named('wrapper', Wrapper) {
				nokee {
					version = '0.4.0'
				}
			}
		'''
	}

	def "can override Nokee version using System property flag"() {
		expect:
		succeeds('wrapper', '-Dnokee-version=0.3.0')
		file('gradle/wrapper/gradle-wrapper.properties').readLines().contains('nokeeVersion=0.3.0')
	}

	def "can override Nokee version using Gradle property flag"() {
		expect:
		succeeds('wrapper', '-Pnokee-version=0.3.0')
		file('gradle/wrapper/gradle-wrapper.properties').readLines().contains('nokeeVersion=0.3.0')
	}
}
