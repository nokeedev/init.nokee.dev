package dev.nokee.init

import dev.gradleplugins.integtests.fixtures.AbstractGradleSpecification
import dev.gradleplugins.runnerkit.GradleWrapperFixture
import org.gradle.api.Plugin
import org.gradle.api.Project

import static dev.nokee.init.fixtures.GradleRunnerUtils.configurePluginClasspathAsInitScriptDependencies;

class GradleWrapperNokeeVersionProviderFunctionalTest extends AbstractGradleSpecification {
	def setup() {
		def initScriptFile = file('init.gradle')
		initScriptFile << configurePluginClasspathAsInitScriptDependencies() << '''
			apply plugin: dev.nokee.init.NokeeInitPlugin
		'''
		executer = executer.usingInitScript(initScriptFile)
	}

	def "takes version from System property"(propertyName) {
		expect:
		succeeds('wrapper', "-D${propertyName}=0.4.0")
		file('gradle/wrapper/gradle-wrapper.properties').readLines().contains('nokeeVersion=0.4.0')

		where:
		propertyName << ['use-nokee-version', 'nokee-version', 'useNokeeVersionFromWrapper']
	}

	def "takes version from Gradle property"(propertyName) {
		expect:
		succeeds('wrapper', "-P${propertyName}=0.4.0")
		file('gradle/wrapper/gradle-wrapper.properties').readLines().contains('nokeeVersion=0.4.0')

		where:
		propertyName << ['use-nokee-version', 'nokee-version']
	}

	def "takes version from environment variable"(variableName) {
		expect:
		executer.withEnvironmentVariable(variableName, '0.4.0').withTasks('wrapper').build()
		file('gradle/wrapper/gradle-wrapper.properties').readLines().contains('nokeeVersion=0.4.0')

		where:
		variableName << ['USE_NOKEE_VERSION', 'NOKEE_VERSION']
	}

	def "takes version from cache file"(fileName) {
		file('.gradle', fileName) << '0.4.0'

		expect:
		succeeds('wrapper')
		file('gradle/wrapper/gradle-wrapper.properties').readLines().contains('nokeeVersion=0.4.0')

		where:
		fileName << ['use-nokee-version.txt', 'nokee-version.txt']
	}

	def "takes version from Gradle wrapper property"(propertyName) {
		GradleWrapperFixture.writeGradleWrapperTo(testDirectory)
		Properties properties = new Properties()
		File propertiesFile = testDirectory.file('gradle/wrapper/gradle-wrapper.properties')
		propertiesFile.withInputStream { inStream ->
			properties.load(inStream)
			properties.put(propertyName, '0.4.0')
			propertiesFile.withOutputStream { outStream ->
				properties.store(outStream, null)
			}
		}

		expect:
		succeeds('wrapper')
		file('gradle/wrapper/gradle-wrapper.properties').readLines().contains('nokeeVersion=0.4.0')

		where:
		propertyName << ['useNokeeVersion', 'nokeeVersion']
	}

	def "takes version from buildscript"() {
		file(settingsFileName) << '''
			buildscript {
				repositories {
					mavenCentral()
					maven { url = 'https://repo.nokeedev.net/release' }
				}
				dependencies {
					classpath platform("dev.nokee:nokee-gradle-plugins:0.4.0")
				}
			}
		'''

		expect:
		succeeds('wrapper')
		file('gradle/wrapper/gradle-wrapper.properties').readLines().contains('nokeeVersion=0.4.0')
	}

	def "takes version from buildSrc dependency"() {
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
		succeeds('wrapper')
		file('gradle/wrapper/gradle-wrapper.properties').readLines().contains('nokeeVersion=0.4.0')
	}

	def "prefers version from buildSrc dependency instead of environment variable"() {
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
		executer.withEnvironmentVariable('NOKEE_VERSION', '0.3.0').withTasks('wrapper').build()
		file('gradle/wrapper/gradle-wrapper.properties').readLines().contains('nokeeVersion=0.4.0')
	}

	def "takes version from included build"() {
		settingsFile << '''
			includeBuild('included-build')
		'''
		file('included-build', buildFileName) << '''
			plugins {
				id 'java-gradle-plugin'
			}

			repositories {
				mavenCentral()
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
		buildFile << """
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
		succeeds('wrapper')
		file('gradle/wrapper/gradle-wrapper.properties').readLines().contains('nokeeVersion=0.4.0')
	}
}
