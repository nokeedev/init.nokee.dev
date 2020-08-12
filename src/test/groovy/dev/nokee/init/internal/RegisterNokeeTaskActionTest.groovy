package dev.nokee.init.internal

import dev.nokee.init.internal.ConfigureNokeeTaskAction
import dev.nokee.init.internal.NokeeTask
import dev.nokee.init.internal.RegisterNokeeTaskAction
import org.gradle.api.Project
import org.gradle.api.tasks.TaskContainer
import spock.lang.Specification
import spock.lang.Subject

@Subject(RegisterNokeeTaskAction)
class RegisterNokeeTaskActionTest extends Specification {
    def subject = new RegisterNokeeTaskAction()

    def "registers Nokee task"() {
        given:
        def tasks = Mock(TaskContainer)
        def project = Mock(Project) {
            getTasks() >> tasks
        }

        when:
        subject.execute(project)

        then:
        1 * tasks.register('nokee', NokeeTask, _) >> { args -> assert args[2] instanceof ConfigureNokeeTaskAction }
    }
}
