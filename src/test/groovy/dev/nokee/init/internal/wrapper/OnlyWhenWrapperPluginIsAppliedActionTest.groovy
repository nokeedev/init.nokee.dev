package dev.nokee.init.internal.wrapper

import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.plugins.AppliedPlugin
import org.gradle.api.plugins.PluginContainer
import org.gradle.buildinit.plugins.WrapperPlugin
import spock.lang.Specification
import spock.lang.Subject

@Subject(OnlyWhenWrapperPluginIsAppliedAction)
class OnlyWhenWrapperPluginIsAppliedActionTest extends Specification {
	def delegate = Mock(Action)
	def subject = new OnlyWhenWrapperPluginIsAppliedAction(delegate)

	def "forwards only when WrapperPlugin is applied"() {
		given:
		def appliedPlugin = Mock(WrapperPlugin)
		def pluginContainer = Mock(PluginContainer)
		def project = Mock(Project) {
			getPlugins() >> pluginContainer
		}

		when:
		subject.execute(project)

		then:
		1 * pluginContainer.withType(WrapperPlugin, _ as Action) >> { args -> args[1].execute(appliedPlugin) }
		1 * delegate.execute(project)
	}
}
