package dev.nokee.init

import dev.nokee.init.internal.GradleVersionProvider
import dev.nokee.init.internal.NokeeInitBuildListener
import dev.nokee.init.internal.RegisterNokeeTaskAction
import org.gradle.api.invocation.Gradle
import org.gradle.util.GradleVersion
import spock.lang.Specification
import spock.lang.Subject

@Subject(NokeeInitPlugin)
class NokeeInitPluginTest extends Specification {
    def "register build listener"() {
        given:
        def subject = new NokeeInitPlugin()
        def gradle = Mock(Gradle)

        when:
        subject.apply(gradle)

        then:
        1 * gradle.addBuildListener(_) >> {args -> assert args[0] instanceof NokeeInitBuildListener }
    }

    def "register root project configuration"() {
        given:
        def subject = new NokeeInitPlugin()
        def gradle = Mock(Gradle)

        when:
        subject.apply(gradle)

        then:
        1 * gradle.rootProject(_) >> {args -> assert args[0] instanceof RegisterNokeeTaskAction }
    }

    def "do nothing on unsupported version"() {
        given:
        def gradleVersionProvider = Mock(GradleVersionProvider)
        def subject = new NokeeInitPlugin(gradleVersionProvider)
        def gradle = Mock(Gradle)

        when:
        subject.apply(gradle)

        then:
        1 * gradleVersionProvider.get() >> GradleVersion.version("6.0")
        0 * gradle._
    }
}
