package dev.nokee.init.internal.wrapper

import dev.nokee.init.internal.accessors.GradlePropertyAccessor
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.api.provider.Property
import org.gradle.api.tasks.wrapper.Wrapper
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification
import spock.lang.Subject

import static dev.nokee.init.internal.utils.FilenameUtils.separatorsToUnix

@Subject(RegisterNokeeWrapperExtensionAction)
class RegisterNokeeWrapperExtensionActionTest extends Specification {
	def project = ProjectBuilder.builder().build()
	def subject = new RegisterNokeeWrapperExtensionAction(project.objects, project.layout, Mock(GradlePropertyAccessor))

	def "registers nokee wrapper extensions"() {
		given:
		def wrapper = project.tasks.create('wrapper', Wrapper)

		when:
		subject.execute(wrapper)

		then:
		wrapper.extensions.nokeeVersion instanceof Property
		wrapper.extensions.nokeeVersion.orNull == null

		and:
		wrapper.extensions.nokeeInitScriptFile instanceof RegularFileProperty
		separatorsToUnix(wrapper.extensions.nokeeInitScriptFile.get().asFile.absolutePath) == "${project.projectDir}/gradle/nokee.init.gradle"

		and:
		wrapper.actions.size() == 2 // includes Nokee patcher
	}
}
