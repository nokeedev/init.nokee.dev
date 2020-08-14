package dev.nokee.init.internal.versions

import dev.nokee.init.internal.accessors.EnvironmentVariableAccessor
import dev.nokee.init.internal.accessors.SystemPropertyAccessor
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification
import spock.lang.Subject

@Subject(DefaultNokeeVersionProvider)
class DefaultNokeeVersionProviderTest extends Specification {
    @Rule TemporaryFolder temporaryFolder = new TemporaryFolder()
    def systemPropertyAccessor = Mock(SystemPropertyAccessor)
    def environmentVariableAccessor = Mock(EnvironmentVariableAccessor)
    def subject = new DefaultNokeeVersionProvider({ temporaryFolder.root }, systemPropertyAccessor, environmentVariableAccessor)

    def "prefers global system property"() {
        when:
        def result = subject.get()

        then:
        1 * systemPropertyAccessor.get('use-nokee-version') >> '0.4.0'
        0 * _

        and:
        result.present
        result.get().toString() == '0.4.0'
    }

    def "prefers global environment variable"() {
        when:
        def result = subject.get()

        then:
        1 * systemPropertyAccessor.get('use-nokee-version') >> null
        1 * environmentVariableAccessor.get('USE_NOKEE_VERSION') >> '0.4.0'
        0 * _

        and:
        result.present
        result.get().toString() == '0.4.0'
    }

    def "prefers cache file"() {
        given:
        temporaryFolder.newFolder('.gradle')
        temporaryFolder.newFile('.gradle/use-nokee-version.txt') << '0.4.0'

        when:
        def result = subject.get()

        then:
        1 * systemPropertyAccessor.get('use-nokee-version') >> null
        1 * environmentVariableAccessor.get('USE_NOKEE_VERSION') >> null
        0 * _

        and:
        result.present
        result.get().toString() == '0.4.0'
    }

    def "prefers wrapper system property"() {
        given:
        assert !new File(temporaryFolder.root, '.gradle/use-nokee-version.txt').exists()

        when:
        def result = subject.get()

        then:
        1 * systemPropertyAccessor.get('use-nokee-version') >> null
        1 * environmentVariableAccessor.get('USE_NOKEE_VERSION') >> null
        1 * systemPropertyAccessor.get('useNokeeVersionFromWrapper') >> '0.4.0'
        0 * _

        and:
        result.present
        result.get().toString() == '0.4.0'
    }

    def "prefers wrapper properties file"() {
        given:
        assert !new File(temporaryFolder.root, '.gradle/use-nokee-version.txt').exists()
        temporaryFolder.newFolder('gradle', 'wrapper')
        temporaryFolder.newFile('gradle/wrapper/gradle-wrapper.properties') << 'useNokeeVersion=0.4.0\n'

        when:
        def result = subject.get()

        then:
        1 * systemPropertyAccessor.get('use-nokee-version') >> null
        1 * environmentVariableAccessor.get('USE_NOKEE_VERSION') >> null
        1 * systemPropertyAccessor.get('useNokeeVersionFromWrapper') >> null
        0 * _

        and:
        result.present
        result.get().toString() == '0.4.0'
    }
}
