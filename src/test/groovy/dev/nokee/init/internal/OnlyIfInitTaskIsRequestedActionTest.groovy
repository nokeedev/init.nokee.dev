package dev.nokee.init.internal

import org.gradle.StartParameter
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.invocation.Gradle
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

@Subject(OnlyIfInitTaskIsRequestedAction)
class OnlyIfInitTaskIsRequestedActionTest extends Specification {
    def startParameter = Mock(StartParameter)
    def gradle = Mock(Gradle) {
        getStartParameter() >> startParameter
    }
    def project = Mock(Project) {
        getGradle() >> gradle
    }
    def action = Mock(Action)
    def subject = new OnlyIfInitTaskIsRequestedAction(action)

    @Unroll
    def "forwards only if init task is requested"(initTask) {
        given:
        startParameter.getTaskNames() >> [initTask]

        when:
        subject.execute(project)

        then:
        1 * action.execute(project)

        where:
        initTask << ['init', ':init']
    }

    def "does not forwards for other task request"() {
        given:
        startParameter.getTaskNames() >> ['foo']

        when:
        subject.execute(project)

        then:
        0 * action.execute(project)
    }
}
