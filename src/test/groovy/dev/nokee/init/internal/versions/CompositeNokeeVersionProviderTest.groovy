package dev.nokee.init.internal.versions


import org.gradle.util.VersionNumber
import spock.lang.Specification
import spock.lang.Subject

@Subject(CompositeNokeeVersionProvider)
class CompositeNokeeVersionProviderTest extends Specification {
	def firstProvider = Mock(NokeeVersionProvider)
	def secondProvider = Mock(NokeeVersionProvider)
	def subject = new CompositeNokeeVersionProvider(firstProvider, secondProvider)

	def "returns the first non-empty result"() {
		given:
		def value = Optional.of(VersionNumber.parse('0.4.0'))

		when:
		def result1 = subject.get()
		then:
		1 * firstProvider.get() >> value
		0 * secondProvider.get()
		and:
		result1 == value

		when:
		def result2 = subject.get()
		then:
		1 * firstProvider.get() >> Optional.empty()
		1 * secondProvider.get() >> value
		and:
		result2 == value
	}

	def "returns empty result if all result returns empty"() {
		when:
		def result = subject.get()

		then:
		1 * firstProvider.get() >> Optional.empty()
		1 * secondProvider.get() >> Optional.empty()

		and:
		!result.isPresent()
	}
}
