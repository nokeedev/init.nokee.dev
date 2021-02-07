package dev.nokee.init.internal.versions

import dev.nokee.init.internal.accessors.EnvironmentVariableAccessor
import spock.lang.Specification
import spock.lang.Subject

@Subject(EnvironmentVariableNokeeVersionProvider)
class EnvironmentVariableNokeeVersionProviderTest extends Specification {
	def accessor = Mock(EnvironmentVariableAccessor)
	def subject = new EnvironmentVariableNokeeVersionProvider(accessor)

	def "returns value from environment variable if available"() {
		when:
		def result = subject.get()

		then:
		1 * accessor.get('USE_NOKEE_VERSION') >> '0.4.0'

		and:
		result.present
		result.get().toString() == '0.4.0'
	}

	def "returns empty when environment variable is absent"() {
		when:
		def result = subject.get()

		then:
		1 * accessor.get('USE_NOKEE_VERSION') >> null

		and:
		!result.present
	}
}
