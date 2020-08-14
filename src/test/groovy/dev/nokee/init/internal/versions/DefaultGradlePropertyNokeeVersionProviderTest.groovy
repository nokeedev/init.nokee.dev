package dev.nokee.init.internal.versions

import dev.nokee.init.internal.accessors.GradlePropertyAccessor
import spock.lang.Specification

class DefaultGradlePropertyNokeeVersionProviderTest extends Specification {
    def accessor = Mock(GradlePropertyAccessor)
    def subject = new DefaultGradlePropertyNokeeVersionProvider(accessor)

    def "returns value from Gradle property if available"() {
        when:
        def result = subject.get()

        then:
        1 * accessor.get('use-nokee-version') >> '0.4.0'

        and:
        result.present
        result.get().toString() == '0.4.0'
    }

    def "returns empty when Gradle property is absent"() {
        when:
        def result = subject.get()

        then:
        1 * accessor.get('use-nokee-version') >> null

        and:
        !result.present
    }
}
