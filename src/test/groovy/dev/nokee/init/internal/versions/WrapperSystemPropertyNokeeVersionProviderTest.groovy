package dev.nokee.init.internal.versions

import dev.nokee.init.internal.accessors.SystemPropertyAccessor
import spock.lang.Specification

class WrapperSystemPropertyNokeeVersionProviderTest extends Specification {
	def accessor = Mock(SystemPropertyAccessor)
	def subject = new WrapperSystemPropertyNokeeVersionProvider(accessor)

	def "returns value from system property if available"() {
		when:
		def result = subject.get()

		then:
		1 * accessor.get('useNokeeVersionFromWrapper') >> '0.4.0'

		and:
		result.present
		result.get().toString() == '0.4.0'
	}

	def "returns empty when system property is absent"() {
		when:
		def result = subject.get()

		then:
		1 * accessor.get('useNokeeVersionFromWrapper') >> null

		and:
		!result.present
	}
}
