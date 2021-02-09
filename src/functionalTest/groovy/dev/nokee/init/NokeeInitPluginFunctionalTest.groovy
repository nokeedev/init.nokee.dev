//package dev.nokee.init
//
//import dev.gradleplugins.integtests.fixtures.AbstractGradleSpecification
//import dev.gradleplugins.runnerkit.GradleExecutor
//import dev.gradleplugins.runnerkit.GradleRunner
//import dev.gradleplugins.runnerkit.GradleWrapperFixture
//import org.gradle.api.Plugin
//import org.gradle.api.Project
//import org.gradle.internal.os.OperatingSystem
//import spock.lang.Ignore
//
//import java.nio.file.Files
//import java.nio.file.StandardCopyOption
//
//class NokeeInitPluginFunctionalTest extends AbstractGradleSpecification {
//	def "can use init script via command line"() {
//		given:
//		def initScript = file('nokee.init.gradle')
//		initScript << configurePluginClasspathAsBuildScriptDependencies().replace('buildscript', 'initscript') << '''
//			apply plugin: dev.nokee.init.NokeeInitPlugin
//		'''
//		usingInitScript(initScript)
//
//		and:
//		file('.gradle/use-nokee-version.txt') << '0.4.0'
//
//		when:
//		def result = succeeds('help')
//
//		then:
//		result.assertOutputContains("Build ':' use Nokee version '0.4.0'.")
//	}
//
//	def "can use init script inside Gradle user home"() {
//		given:
//		executer = executer./*requireIsolatedDaemons().*/withGradleUserHomeDirectory(testDirectory)
//		def initScript = file('init.d/nokee.init.gradle')
//		initScript << configurePluginClasspathAsBuildScriptDependencies().replace('buildscript', 'initscript') << '''
//			apply plugin: dev.nokee.init.NokeeInitPlugin
//		'''
//
//		and:
//		file('.gradle/use-nokee-version.txt') << '0.4.0'
//
//		when:
//		def result = succeeds('help')
//
//		then:
//		result.assertOutputContains("Build ':' use Nokee version '0.4.0'.")
//	}
//
//	def "can use nokee version of host build from included build"() {
//		given:
//		settingsFile << '''
//			includeBuild 'child'
//		'''
//		file('child', settingsFileName).createFile()
//
//		and:
//		def initScript = file('nokee.init.gradle')
//		initScript << configurePluginClasspathAsBuildScriptDependencies().replace('buildscript', 'initscript') << '''
//			apply plugin: dev.nokee.init.NokeeInitPlugin
//		'''
//		usingInitScript(initScript)
//
//		and:
//		file('.gradle/use-nokee-version.txt') << '0.4.0'
//
//		when:
//		def result = succeeds('help')
//
//		then:
//		result.assertOutputContains("Build ':' use Nokee version '0.4.0'.")
//		result.assertOutputContains("Build ':child' use Nokee version '0.4.0'.")
//	}
//
//	def "shows help if nokee task executes without options"() {
//		def initScript = file('nokee.init.gradle')
//		initScript << configurePluginClasspathAsBuildScriptDependencies().replace('buildscript', 'initscript') << '''
//			apply plugin: dev.nokee.init.NokeeInitPlugin
//		'''
//		usingInitScript(initScript)
//
//		when:
//		def result = succeeds('nokee')
//
//		then:
//		result.assertOutputContains('USAGE: gradlew nokee [option...]')
//	}
//
//	def "shows version if nokee task executes with --show-version flag"() {
//		given:
//		def initScript = file('nokee.init.gradle')
//		initScript << configurePluginClasspathAsBuildScriptDependencies().replace('buildscript', 'initscript') << '''
//			apply plugin: dev.nokee.init.NokeeInitPlugin
//		'''
//		usingInitScript(initScript)
//
//		and:
//		file('.gradle/use-nokee-version.txt') << '0.4.0'
//
//		when:
//		def result = succeeds('nokee', '--show-version')
//
//		then:
//		result.assertOutputContains("Build ':' use Nokee version '0.4.0'.")
//		result.assertOutputContains('Using Nokee 0.4.0')
//	}
//
//	def "shows help if nokee task executes with --show-help flag"() {
//		def initScript = file('nokee.init.gradle')
//		initScript << configurePluginClasspathAsBuildScriptDependencies().replace('buildscript', 'initscript') << '''
//			apply plugin: dev.nokee.init.NokeeInitPlugin
//		'''
//		usingInitScript(initScript)
//
//		when:
//		def result = succeeds('nokee', '--show-help')
//
//		then:
//		result.assertOutputContains('USAGE: gradlew nokee [option...]')
//	}
//
//	// TODO: Move to initscript testing
//	def "well-behaved plugin with dependency verification enabled"() {
//		given:
//		def initScript = file('nokee.init.gradle')
//		writeInitScript(initScript)
//		executer = executer.usingInitScript(initScript).requireOwnGradleUserHomeDirectory()
//		file('gradle/verification-metadata.xml') << '''<?xml version="1.0" encoding="UTF-8"?>
//<verification-metadata>
//   <configuration>
//	  <verify-metadata>false</verify-metadata>
//	  <verify-signatures>true</verify-signatures>
//	</configuration>
//</verification-metadata>
//'''
//		file('gradle.properties') << '''org.gradle.dependency.verification=strict
//'''
//
//		expect:
//		succeeds('tasks')
//	}
//
//	def "buildSrc"() {
//		given:
//		def initScript = file('nokee.init.gradle')
//		initScript << configurePluginClasspathAsBuildScriptDependencies().replace('buildscript', 'initscript') << '''
//println("INSIDE INIT SCRIPT")
//			apply plugin: dev.nokee.init.NokeeInitPlugin
//		'''
////		usingInitScript(initScript)
////		file('.gradle')
////		file('.gradle/use-nokee-version.txt') << '0.4.0'
//
//		and:
//		file('buildSrc/build.gradle') << """
//			plugins {
//				id 'java-gradle-plugin'
//			}
//			repositories {
//				jcenter()
//				maven { url = 'https://dl.bintray.com/nokeedev/distributions' }
//			}
//
//			gradlePlugin {
//				plugins {
//					bob {
//						id = 'bob'
//						implementationClass = 'com.example.BobPlugin'
//					}
//				}
//			}
//
//			dependencies {
//				implementation platform("dev.nokee:nokee-gradle-plugins:0.4.0")
//			}
//		"""
//		file('buildSrc/src/main/java/com/example/BobPlugin.java') << """
//			package com.example;
//
//			import ${Plugin.canonicalName};
//			import ${Project.canonicalName};
//
//			public class BobPlugin implements Plugin<Project> {
//				public void apply(Project project) {}
//			}
//		"""
//		file('buildSrc/settings.gradle') << """
//			println('hey buildsrc settings')
//			println(settings.getGradle().getStartParameter().getAllInitScripts())
//		"""
//		settingsFile << """println('bob')
//println(settings.getGradle().getStartParameter().getAllInitScripts())
//			plugins.withId('com.gradle.enterprise') {
//    gradleEnterprise {
//        buildScan {
//            termsOfServiceUrl = "https://gradle.com/terms-of-service"
//            termsOfServiceAgree = "yes"
//        }
//    }
//}
//		"""
//		buildFile << """
//			plugins {
//				id 'bob'
//				id 'dev.nokee.jni-library'
//			}
//		"""
//		GradleWrapperFixture.writeGradleWrapperTo(testDirectory)
//
//		when:
//		def result = succeeds('help', '-i')
////		def result = GradleRunner.create(GradleExecutor.gradleWrapper()).inDirectory(testDirectory).usingInitScript(initScript).withTasks('help').withArgument('-Duse-nokee-version=0.3.0').publishBuildScans().build()
////		def result = succeeds('help')
//
//		then:
//		false
//
////		then:
////		result.assertOutputContains("Build ':' use Nokee version '0.4.0'.")
//	}
//
//
//
//	// VERSION DETECTION
//	// no init.d script buildSrc, dependency on master artifact in buildSrc/build.gradle -> show version from classpath
//	// There is a chance that included build could also inject a build classpath version
//	// -> Will need to check if nested build can inject different nokee version in which case we will need to fail
//	// check System properties
//	// check Gradle properties
//	// check wrapper properties // let's ignored and let it erase in next generation
//	// check gradle/wrapper/gradle-wrapper.properties
//	// (one override the other and should mention in in the info log)
//	// If any other sources is specified when version is detected from build classpath, fail. (will need to fail during root project execution)
//	// If any source (system, gradle prop) differ from gradle-wrapper.properties mention in lifecycle and use that one
//	//
//
//	// TODO: Pull dependency from buildSrc (can detect version miss match in host build, no configuration required in host build when applying plugin)
//	// TODO: pull dependency from included build ( can detect version miss match in host build, no configuration required in host build when pulling the classpath)
//	// TODO:
//
//	// TODO: when init script pass through command line, init.d script should self disable (with a lot to the info console)
//	// TODO: to align with everything else, we should always self disable init script from init.d in buildSrc (named buildSrc with a parent build).
//
//	void writeInitScript(File initscript) {
//		Files.copy(NokeeInitPluginFunctionalTest.class.getResourceAsStream("/dev/nokee/init/internal/wrapper/nokee.init.gradle"), initscript.toPath(), StandardCopyOption.REPLACE_EXISTING)
//	}
//}
