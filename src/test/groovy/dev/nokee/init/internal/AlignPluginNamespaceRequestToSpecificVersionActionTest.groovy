package dev.nokee.init.internal

import org.gradle.plugin.management.PluginRequest
import org.gradle.plugin.management.PluginResolveDetails
import org.gradle.plugin.use.PluginId
import org.gradle.util.VersionNumber
import spock.lang.Specification
import spock.lang.Subject

@Subject(AlignPluginNamespaceRequestToSpecificVersionAction)
class AlignPluginNamespaceRequestToSpecificVersionActionTest extends Specification {
	def "can align any plugins under specified plugin namespace to specified version"() {
		given:
		def subject = new AlignPluginNamespaceRequestToSpecificVersionAction('dev.nokee.', VersionNumber.parse('1.2.3'))
		def details = detailsFor('dev.nokee.cpp-library')

		when:
		subject.execute(details)

		then:
		1 * details.useVersion('1.2.3')
	}

	def "does not align plugins outside of namespace"() {
		given:
		def subject = new AlignPluginNamespaceRequestToSpecificVersionAction('dev.nokee.', VersionNumber.parse('1.2.3'))
		def details = detailsFor('org.gradle.cpp-library')

		when:
		subject.execute(details)

		then:
		0 * details.useVersion(_)
	}

	PluginResolveDetails detailsFor(String pluginId) {
		return Mock(PluginResolveDetails) {
			getRequested() >> Mock(PluginRequest) {
				getId() >> Mock(PluginId) {
					getId() >> pluginId
				}
			}
		}
	}
}
