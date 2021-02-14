package dev.nokee.init

import dev.gradleplugins.integtests.fixtures.AbstractGradleSpecification

import java.nio.file.Files

import static dev.gradleplugins.runnerkit.GradleExecutor.gradleWrapper
import static dev.gradleplugins.runnerkit.GradleRunner.create
import static dev.nokee.init.fixtures.GradleRunnerUtils.configurePluginClasspathAsInitScriptDependencies

class GradleWrapperLenientPatchingFunctionalTest extends AbstractGradleSpecification {
	def setup() {
		def initScriptFile = file('init.gradle')
		initScriptFile << configurePluginClasspathAsInitScriptDependencies() << '''
			apply plugin: dev.nokee.init.NokeeInitPlugin
		'''
		executer.usingInitScript(initScriptFile).withArgument('-Dnokee-version').withTasks('wrapper').build()
		create(gradleWrapper()).inDirectory(testDirectory).withTasks('nokee').build()
	}

	def "can delete nokee.init.gradle without breaking the build"() {
		file('gradle/nokee.init.gradle').delete()
		println Files.exists(file('gradle/nokee.init.gradle').toPath())
		println(new String(Files.readAllBytes(file('gradle/nokee.init.gradle').toPath())))

		expect:
		def result = create(gradleWrapper()).inDirectory(testDirectory).withTasks('tasks').build()
		!result.output.contains('nokee')
	}
}
