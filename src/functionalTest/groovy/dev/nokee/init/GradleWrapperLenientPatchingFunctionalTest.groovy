package dev.nokee.init

import dev.gradleplugins.integtests.fixtures.AbstractGradleSpecification

import java.nio.file.FileVisitResult
import java.nio.file.FileVisitor
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.attribute.BasicFileAttributes

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
		Files.walkFileTree(testDirectory, new FileVisitor<Path>() {
			@Override
			FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
				return FileVisitResult.CONTINUE
			}

			@Override
			FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				println(file)
				return FileVisitResult.CONTINUE
			}

			@Override
			FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
				return FileVisitResult.CONTINUE
			}

			@Override
			FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
				return FileVisitResult.CONTINUE
			}
		})

		expect:
		def result = create(gradleWrapper()).inDirectory(testDirectory).withTasks('tasks').build()
		!result.output.contains('nokee')
	}
}
