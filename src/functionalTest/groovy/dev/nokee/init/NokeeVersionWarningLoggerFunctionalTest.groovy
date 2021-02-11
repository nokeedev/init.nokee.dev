package dev.nokee.init

import dev.gradleplugins.integtests.fixtures.AbstractGradleSpecification

import static dev.nokee.init.fixtures.GradleRunnerUtils.configurePluginClasspathAsInitScriptDependencies

class NokeeVersionWarningLoggerFunctionalTest extends AbstractGradleSpecification {
	def setup() {
		def initScriptFile = file('init.gradle')
		initScriptFile << configurePluginClasspathAsInitScriptDependencies() << '''
			apply plugin: dev.nokee.init.NokeeInitPlugin
		'''
		executer = executer.usingInitScript(initScriptFile)
	}

	def "warns only once about version conflict"() {
		buildFile << '''
			tasks.register('resolveVersion') {
				doLast {
					nokee.version.get()
					nokee.version.get()
				}
			}
		'''

		expect:
		def result = succeeds('resolveVersion', '-Dnokee-version=0.3.0', '-Pnokee-version=0.4.0')
		result.output.count("WARNING: version '0.3.0' (from System property) overrides version '0.4.0' (from Gradle property).") == 1
	}

	def "warns only once about version deprecation"() {
		buildFile << '''
			tasks.register('resolveVersion') {
				doLast {
					nokee.version.get()
					nokee.version.get()
				}
			}
		'''

		expect:
		def result = succeeds('resolveVersion', '-Duse-nokee-version=0.4.0', '-Pnokee-version=0.4.0')
		result.output.count("WARNING: Use nokee-version System property instead.") == 1
	}
}
