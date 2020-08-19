package dev.nokee.init.internal.accessors

import org.gradle.api.Project
import spock.lang.Specification

class DefaultGradlePropertyAccessorTest extends Specification {
    def project = Mock(Project)
    def subject = new DefaultGradlePropertyAccessor(project)

    def "returns null when property is absent"() {
        when:
        def result = subject.get('missing')

        then:
        1 * project.findProperty('missing') >> null

        and:
        result == null
    }

    def "returns toString representation of the object"() {
        given:
        def object = Mock(PropertyObject)

        when:
        def result = subject.get('existing')

        then:
        1 * project.findProperty('existing') >> object
        1 * object.toString() >> 'foo'

        and:
        result == 'foo'
    }

    interface PropertyObject {}
}
