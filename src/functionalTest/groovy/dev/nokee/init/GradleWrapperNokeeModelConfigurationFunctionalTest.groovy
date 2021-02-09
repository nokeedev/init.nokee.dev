package dev.nokee.init

import dev.gradleplugins.integtests.fixtures.AbstractGradleSpecification

import static dev.nokee.init.fixtures.GradleRunnerUtils.configurePluginClasspathAsInitScriptDependencies

class GradleWrapperNokeeModelConfigurationFunctionalTest extends AbstractGradleSpecification {
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

	def "can configure Nokee version"() {
		buildFile << '''
			tasks.named('wrapper', Wrapper) {
				nokee {
					version = '0.3.0'
				}
			}
		'''

		expect:
		succeeds('wrapper')
		file('gradle/wrapper/gradle-wrapper.properties').readLines().contains('nokeeVersion=0.3.0')
		file('gradle/nokee.init.gradle').exists()
	}

	def "can configure Nokee init script"() {
		buildFile << '''
			tasks.named('wrapper', Wrapper) {
				nokee {
					initScriptFile = file('foo.init.gradle')
				}
			}
		'''

		expect:
		succeeds('wrapper')
		!file('gradle/nokee.init.gradle').exists()
		file('foo.init.gradle').exists()
		file('gradlew').readLines().find {it.contains('foo.init.gradle') }
		file('gradlew.bat').readLines().find {it.contains('foo.init.gradle') }
	}

	def "can override Nokee version using any version provider"() {
		expect:
		succeeds('wrapper', '-Dnokee-version=0.3.0')
		file('gradle/wrapper/gradle-wrapper.properties').readLines().contains('nokeeVersion=0.3.0')
	}
}
