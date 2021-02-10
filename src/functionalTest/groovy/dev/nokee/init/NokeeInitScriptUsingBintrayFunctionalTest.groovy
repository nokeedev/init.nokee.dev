package dev.nokee.init

import dev.gradleplugins.integtests.fixtures.AbstractGradleSpecification

import java.nio.file.Files

import static dev.nokee.init.fixtures.GradleRunnerUtils.asQuotedAbsolutePathSpread
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING

class NokeeInitScriptUsingBintrayFunctionalTest extends AbstractGradleSpecification {
	def "init script in init.d directory"() {
		def initScriptFile = file('user-home/init.d/nokee.init.gradle')
		initScriptFile << bintrayEnabledNokeeInitScriptContent
		executer = executer.withGradleUserHomeDirectory(file('user-home'))

		expect:
		def result = succeeds('help', '-w')
		result.output.contains("Please update init script '${file('user-home/init.d/nokee.init.gradle')}' to a newer version.\nLearn more at https://github.com/nokeedev/init.nokee.dev#bintray-deprecation")
	}

	def "init script as CLI init-script"() {
		def initScriptFile = file('nokee.init.gradle')
		initScriptFile << bintrayEnabledNokeeInitScriptContent
		executer = executer.usingInitScript(initScriptFile)

		expect:
		def result = succeeds('help', '-w')
		result.output.contains("Please update init script '${file('nokee.init.gradle')}' to a newer version.\nLearn more at https://github.com/nokeedev/init.nokee.dev#bintray-deprecation")
	}

	private static String getBintrayEnabledNokeeInitScriptContent() {
		return """
			initscript {
				repositories {
					maven { url = 'https://dl.bintray.com/nokeedev/distributions-initialization' }
					mavenLocal {
						content {
							it.includeModule('dev.nokee.init', 'init.nokee.dev')
						}
					}
				}
				dependencies {
					classpath(files(${asQuotedAbsolutePathSpread(getImplementationClassPath())}))
				}
			}
			apply plugin: dev.nokee.init.NokeeInitPlugin
		"""
	}
}
