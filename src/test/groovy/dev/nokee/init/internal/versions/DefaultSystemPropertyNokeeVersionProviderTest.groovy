package dev.nokee.init.internal.versions

import dev.nokee.init.internal.accessors.SystemPropertyAccessor
import spock.lang.Specification
import spock.lang.Subject

@Subject(DefaultSystemPropertyNokeeVersionProvider)
class DefaultSystemPropertyNokeeVersionProviderTest extends Specification {
	def accessor = Mock(SystemPropertyAccessor)
	def subject = new DefaultSystemPropertyNokeeVersionProvider(accessor)

	def "returns value from system property if available"() {
		when:
		def result = subject.get()

		then:
		1 * accessor.get('use-nokee-version') >> '0.4.0'

		and:
		result.present
		result.get().toString() == '0.4.0'
	}

	def "returns empty when system property is absent"() {
		when:
		def result = subject.get()

		then:
		1 * accessor.get('use-nokee-version') >> null

		and:
		!result.present
	}
}
