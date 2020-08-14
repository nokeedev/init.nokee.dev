package dev.nokee.init

import dev.nokee.init.internal.versions.GradleVersionProvider
import dev.nokee.init.internal.NokeeInitBuildListener
import dev.nokee.init.internal.OnlyIfInitTaskIsRequestedAction
import dev.nokee.init.internal.RegisterNokeeTaskAction
import dev.nokee.init.internal.wrapper.OnlyWhenWrapperPluginIsAppliedAction
import org.gradle.api.invocation.Gradle
import org.gradle.util.GradleVersion
import spock.lang.Specification
import spock.lang.Subject

@Subject(NokeeInitPlugin)
class NokeeInitPluginTest extends Specification {
    def gradle = Mock(Gradle)

    def "register build listener"() {
        given:
        def subject = new NokeeInitPlugin()

        when:
        subject.apply(gradle)

        then:
        1 * gradle.addBuildListener(_ as NokeeInitBuildListener)
    }

    def "register root project configuration"() {
        given:
        def subject = new NokeeInitPlugin()

        when:
        subject.apply(gradle)

        then:
        1 * gradle.rootProject(_ as RegisterNokeeTaskAction)
    }

    def "do nothing on unsupported version"() {
        given:
        def gradleVersionProvider = Mock(GradleVersionProvider)
        def subject = new NokeeInitPlugin(gradleVersionProvider)

        when:
        subject.apply(gradle)

        then:
        1 * gradleVersionProvider.get() >> GradleVersion.version("6.0")
        0 * gradle._
    }

    def "register build init gateway configuration action"() {
        given:
        def subject = new NokeeInitPlugin()

        when:
        subject.apply(gradle)

        then:
        1 * gradle.rootProject(_ as OnlyIfInitTaskIsRequestedAction)
    }

    def "register wrapper gateway configuration action"() {
        given:
        def subject = new NokeeInitPlugin()

        when:
        subject.apply(gradle)

        then:
        1 * gradle.rootProject(_ as OnlyWhenWrapperPluginIsAppliedAction)
    }
}
