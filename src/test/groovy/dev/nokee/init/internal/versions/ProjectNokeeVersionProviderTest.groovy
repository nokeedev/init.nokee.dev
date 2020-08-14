package dev.nokee.init.internal.versions

import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification
import spock.lang.Subject

@Subject(ProjectNokeeVersionProvider)
class ProjectNokeeVersionProviderTest extends Specification {
    @Rule TemporaryFolder temporaryFolder = new TemporaryFolder()
    def subject = new ProjectNokeeVersionProvider({ temporaryFolder.root })


    def "can read version for specific project"() {
        when:
        temporaryFolder.newFolder('.gradle')
        def versionFile = temporaryFolder.newFile('.gradle/use-nokee-version.txt')
        versionFile << '1.2.3-deadbeef'
        def version = subject.get()

        then:
        version.present
        version.get().major == 1
        version.get().minor == 2
        version.get().micro == 3
        version.get().qualifier == 'deadbeef'
    }

    def "trims newlines from the version file"() {
        when:
        temporaryFolder.newFolder('.gradle')
        def versionFile = temporaryFolder.newFile('.gradle/use-nokee-version.txt')
        versionFile << '1.2.3-deadbeef\n'
        def version = subject.get()

        then:
        version.present
        version.get().major == 1
        version.get().minor == 2
        version.get().micro == 3
        version.get().qualifier == 'deadbeef'
    }

    def "returns empty version when version file does not exists"() {
        when:
        def version = subject.get()

        then:
        noExceptionThrown()

        and:
        !version.present
    }
}
