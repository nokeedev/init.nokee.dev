package dev.nokee.init.internal

import dev.nokee.init.internal.ConfigureNokeeTaskAction
import dev.nokee.init.internal.NokeeTask
import spock.lang.Specification
import spock.lang.Subject

@Subject(ConfigureNokeeTaskAction)
class ConfigureNokeeTaskActionTest extends Specification {
    def subject = new ConfigureNokeeTaskAction();

    def "configures task description"() {
        given:
        def task = Mock(NokeeTask)

        when:
        subject.execute(task)

        then:
        1 * task.setDescription("Configures Gradle integration with Nokee plugins.")
    }

    def "configures task group to build setup"() {
        given:
        def task = Mock(NokeeTask)

        when:
        subject.execute(task)

        then:
        1 * task.setGroup("Build Setup")
    }
}
