package dev.nokee.init

import dev.gradleplugins.integtests.fixtures.AbstractGradleSpecification
import dev.gradleplugins.runnerkit.GradleWrapperFixture
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.junit.Assume
import spock.lang.Ignore

import static org.hamcrest.Matchers.containsString
import static org.hamcrest.Matchers.not
import static org.junit.Assume.assumeThat

abstract class AbstractNokeeVersionDetectionFunctionalTest extends AbstractGradleSpecification {
	private File initScript
	def setup() {
		initScript = testDirectory.file('nokee.init.gradle')
		initScript << configurePluginClasspathAsBuildScriptDependencies().replace('buildscript', 'initscript') << '''
			apply plugin: dev.nokee.init.NokeeInitPlugin
		'''
		usingInitScript(initScript)
	}

	def "can detect Nokee version from System property"() {
		expect:
		def result = succeeds('nokee', showVersionFlag, '-Dnokee-version=0.4.0')
		result.output.contains("Build '${buildPathUnderTest}' using Nokee version '0.4.0' (from System property).")
	}

	def "can detect Nokee version from Gradle property"() {
		expect:
		def result = succeeds('nokee', showVersionFlag, '-Pnokee-version=0.4.0')
		result.output.contains("Build '${buildPathUnderTest}' using Nokee version '0.4.0' (from Gradle property).")
	}

	def "can detect Nokee version from Gradle property as environment variable"() {
		expect:
		def result = executer.withEnvironmentVariable('ORG_GRADLE_PROJECT_nokee-version', '0.4.0').withTasks('nokee', showVersionFlag).build()
		result.output.contains("Build '${buildPathUnderTest}' using Nokee version '0.4.0' (from Gradle property).")
	}

	def "can detect Nokee version from environment variable"() {
		expect:
		def result = executer.withEnvironmentVariable('NOKEE_VERSION', '0.4.0').withTasks('nokee', showVersionFlag).build()
		result.output.contains("Build '${buildPathUnderTest}' using Nokee version '0.4.0' (from environment variable).")
	}

	def "can detect Nokee version from Gradle wrapper properties"() {
		GradleWrapperFixture.writeGradleWrapperTo(testDirectory)
		Properties properties = new Properties()
		File propertiesFile = testDirectory.file('gradle/wrapper/gradle-wrapper.properties')
		propertiesFile.withInputStream { inStream ->
			properties.load(inStream)
			properties.put('nokeeVersion', '0.4.0')
			propertiesFile.withOutputStream { outStream ->
				properties.store(outStream, null)
			}
		}

		expect:
		def result = succeeds('nokee', showVersionFlag)
		result.output.contains("Build '${buildPathUnderTest}' using Nokee version '0.4.0' (from Gradle wrapper property).")
	}

	def "can detect Nokee version from cache file"() {
		testDirectory.file('.gradle/nokee-version.txt') << '0.4.0'

		expect:
		def result = succeeds('nokee', showVersionFlag)
		result.output.contains("Build '${buildPathUnderTest}' using Nokee version '0.4.0' (from cache file).")
	}

	def "can detect Nokee version from build classpath configured via buildSrc"() {
		file('buildSrc', buildFileName) << '''
			repositories {
				jcenter()
				maven { url = 'https://repo.nokeedev.net/release' }
			}

			dependencies {
				implementation platform("dev.nokee:nokee-gradle-plugins:0.4.0")
			}
		'''

		expect:
		def result = succeeds('nokee', showVersionFlag)
		result.output.contains("Build '${buildPathUnderTest}' using Nokee version '0.4.0' (from build classpath).")
	}

	def "can detect Nokee snapshot version from build classpath"() {
		file('buildSrc', buildFileName) << '''
			repositories {
				jcenter()
				maven { url = 'https://repo.nokeedev.net/snapshot' }
			}

			dependencies {
				implementation platform("dev.nokee:nokee-gradle-plugins:0.5.0-12c22234")
			}
		'''

		expect:
		def result = succeeds('nokee', showVersionFlag)
		result.output.contains("Build '${buildPathUnderTest}' using Nokee version '0.5.0-12c22234' (from build classpath).")
	}

	@Ignore('for now as it cause issue with version alignments')
	def "can detect Nokee version from build classpath configured via settings buildscript"() {
		file(settingsFileName) << '''
			buildscript {
				repositories {
					jcenter()
					maven { url = 'https://repo.nokeedev.net/release' }
				}
				dependencies {
					classpath platform("dev.nokee:nokee-gradle-plugins:0.4.0")
				}
			}
		'''

		expect:
		def result = succeeds('nokee', showVersionFlag)
		result.output.contains("Build '${buildPathUnderTest}' using Nokee version '0.4.0' (from buildscript classpath).")
	}

	def "can detect Nokee version from build classpath configured via included build"() {
		assumeThat('nested included build providing a plugin seems to be broken, will debug later',
			getClass().simpleName, not(containsString("IncludedBuild")))

		file(settingsFileName) << '''
			includeBuild('included-build')
		'''
		file('included-build', buildFileName) << '''
			plugins {
				id 'java-gradle-plugin'
			}

			repositories {
				jcenter()
				maven { url = 'https://repo.nokeedev.net/release' }
			}

			dependencies {
				implementation platform("dev.nokee:nokee-gradle-plugins:0.4.0")
			}

			gradlePlugin {
				plugins {
					bob {
						id = 'bob'
						implementationClass = 'com.example.BobPlugin'
					}
				}
			}
		'''
		file('included-build', settingsFileName).createFile()
		file(buildFileName) << """
			plugins {
				id 'bob' // Required to load the included build's runtime into host project
			}
		"""
		file('included-build/src/main/java/com/example/BobPlugin.java') << """
			package com.example;

			import ${Plugin.canonicalName};
			import ${Project.canonicalName};

			public class BobPlugin implements Plugin<Project> {
				public void apply(Project project) {}
			}
		"""

		expect:
		def result = succeeds('nokee', showVersionFlag)
		result.output.contains("Build '${buildPathUnderTest}' using Nokee version '0.4.0' (from build classpath).")
	}

	def "can detect Nokee version from previous System property and warn its usage"() {
		expect:
		def result = succeeds('nokee', showVersionFlag, '-Duse-nokee-version=0.4.0', '-w')
		result.output.contains("Build '${buildPathUnderTest}' using Nokee version '0.4.0' (from System property).")
		result.output.contains("WARNING: Use nokee-version System property instead.")
	}

	def "can detect Nokee version from previous embedded Gradle wrapper System property and warn its usage"() {
		expect:
		def result = succeeds('nokee', showVersionFlag, '-DuseNokeeVersionFromWrapper=0.4.0', '-w')
		result.output.contains("Build '${buildPathUnderTest}' using Nokee version '0.4.0' (from System property).")
		result.output.contains("WARNING: Regenerate your wrapper ./gradlew wrapper.")
	}

	def "can detect Nokee version from previous Gradle property and warn its usage"() {
		expect:
		def result = succeeds('nokee', showVersionFlag, '-Puse-nokee-version=0.4.0', '-w')
		result.output.contains("Build '${buildPathUnderTest}' using Nokee version '0.4.0' (from Gradle property).")
		result.output.contains("WARNING: Use nokee-version Gradle property instead.")
	}

	def "can detect Nokee version from previous environment variable and warn its usage"() {
		expect:
		def result = executer.withEnvironmentVariable('USE_NOKEE_VERSION', '0.4.0').withTasks('nokee', showVersionFlag, '-w').build()
		result.output.contains("Build '${buildPathUnderTest}' using Nokee version '0.4.0' (from environment variable).")
		result.output.contains("WARNING: Use NOKEE_VERSION environment variable instead.")
	}

	def "can detect Nokee version from previous Gradle wrapper properties and warn its usage"() {
		GradleWrapperFixture.writeGradleWrapperTo(testDirectory)
		Properties properties = new Properties()
		File propertiesFile = testDirectory.file('gradle/wrapper/gradle-wrapper.properties')
		propertiesFile.withInputStream { inStream ->
			properties.load(inStream)
			properties.put('useNokeeVersion', '0.4.0')
			propertiesFile.withOutputStream { outStream ->
				properties.store(outStream, null)
			}
		}

		expect:
		def result = succeeds('nokee', showVersionFlag)
		result.output.contains("Build '${buildPathUnderTest}' using Nokee version '0.4.0' (from Gradle wrapper property).")
	}

	def "can detect Nokee version from previous cache file"() {
		testDirectory.file('.gradle/use-nokee-version.txt') << '0.4.0'

		expect:
		def result = succeeds('nokee', showVersionFlag, '-w')
		result.output.contains("Build '${buildPathUnderTest}' using Nokee version '0.4.0' (from cache file).")
		result.output.contains("WARNING: Use .gradle/nokee-version.txt cache file instead.")
	}

	protected abstract String getShowVersionFlag()

	protected abstract String getBuildPathUnderTest()
}
