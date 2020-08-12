package dev.nokee.init.internal

import org.gradle.util.VersionNumber
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

class ProjectNokeeVersionWriterTest extends Specification {
    @Rule TemporaryFolder temporaryFolder = new TemporaryFolder()
    def subject = new ProjectNokeeVersionWriter({ temporaryFolder.root })

    def "can write version for specific project"() {
        when:
        def versionFile = new File(temporaryFolder.root, '.gradle/use-nokee-version.txt')
        subject.write(VersionNumber.parse('1.2.3'))

        then:
        versionFile.text == '1.2.3'
    }

    def "can overwrite version for specific project"() {
        when:
        temporaryFolder.newFolder('.gradle')
        def versionFile = temporaryFolder.newFile('.gradle/use-nokee-version.txt')
        versionFile << '1.2.3-deadbeef'
        subject.write(VersionNumber.parse('1.2.3'))

        then:
        versionFile.text == '1.2.3'
    }
}
