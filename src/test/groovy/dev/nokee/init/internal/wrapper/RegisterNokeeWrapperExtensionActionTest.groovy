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
        def extensions = Mock(ExtensionContainer)
        def wrapper = Mock(Wrapper) {
            getExtensions() >> extensions
        }

        when:
        subject.execute(wrapper)

        then:
        1 * extensions.add('nokeeVersion', _ as Property) >> { args ->
            assert args[1].orNull == null
        }
        1 * extensions.add('nokeeInitScriptFile', _ as RegularFileProperty) >> { args ->
            assert separatorsToUnix(args[1].get().asFile.absolutePath) == "${project.projectDir}/gradle/nokee.init.gradle"
        }
        1 * wrapper.doLast(_ as WriteNokeeVersionConfigurationAction)
    }
}
