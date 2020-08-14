package dev.nokee.init.internal.wrapper

import org.gradle.api.Project
import org.gradle.api.tasks.TaskContainer
import org.gradle.api.tasks.wrapper.Wrapper
import spock.lang.Specification
import spock.lang.Subject

@Subject(RegisterWrapperTaskConfigurationAction)
class RegisterWrapperTaskConfigurationActionTest extends Specification {
    def subject = new RegisterWrapperTaskConfigurationAction()

    def "register configuration action for wrapper task"() {
        given:
        def taskContainer = Mock(TaskContainer)
        def project = Mock(Project) {
            getTasks() >> taskContainer
        }

        when:
        subject.execute(project)

        then:
        1 * taskContainer.named('wrapper', Wrapper, _ as RegisterNokeeWrapperExtensionAction)
    }
}
