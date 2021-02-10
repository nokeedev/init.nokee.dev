package dev.nokee.init

import dev.gradleplugins.integtests.fixtures.AbstractGradleSpecification

import java.nio.file.Files
import java.nio.file.StandardCopyOption

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING

class NokeeInitScriptFunctionalTest extends AbstractGradleSpecification {
	def setup() {
		def initScriptFile = file('nokee.init.gradle')
		Files.copy(NokeeInitScriptFunctionalTest.class.getResourceAsStream("/dev/nokee/init/internal/wrapper/nokee.init.gradle"),
			initScriptFile.toPath(), REPLACE_EXISTING)
		executer = executer.usingInitScript(initScriptFile)
	}

	def "well-behaved plugin with dependency verification enabled"() {
		given:
//		executer = executer.requireOwnGradleUserHomeDirectory()
//		executer = executer.usingInitScript(initScript)//.requireOwnGradleUserHomeDirectory()
		file('gradle/verification-metadata.xml') << '''<?xml version="1.0" encoding="UTF-8"?>
			|<verification-metadata>
			|   <configuration>
			|	  <verify-metadata>false</verify-metadata>
			|	  <verify-signatures>true</verify-signatures>
			|	</configuration>
			|</verification-metadata>
			|'''.stripMargin()
		file('gradle.properties') << 'org.gradle.dependency.verification=strict'

		expect:
		succeeds('tasks')
	}
}
