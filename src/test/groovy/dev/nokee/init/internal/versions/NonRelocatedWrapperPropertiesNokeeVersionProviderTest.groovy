package dev.nokee.init.internal.versions

import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification
import spock.lang.Subject

@Subject(NonRelocatedWrapperPropertiesNokeeVersionProvider)
class NonRelocatedWrapperPropertiesNokeeVersionProviderTest extends Specification {
    @Rule TemporaryFolder temporaryFolder = new TemporaryFolder()
    def subject = new NonRelocatedWrapperPropertiesNokeeVersionProvider({ temporaryFolder.root })

    def "returns the value found in the wrapper properties"() {
        given:
        temporaryFolder.newFolder('gradle', 'wrapper')
        temporaryFolder.newFile('gradle/wrapper/gradle-wrapper.properties') << 'useNokeeVersion=0.4.0\n'
        
        when:
        def result = subject.get()
        
        then:
        result.present
        result.get().toString() == '0.4.0'
    }

    def "returns empty when no value found in the wrapper properties"() {
        given:
        temporaryFolder.newFolder('gradle', 'wrapper')
        temporaryFolder.newFile('gradle/wrapper/gradle-wrapper.properties')

        when:
        def result = subject.get()

        then:
        !result.present
    }

    def "returns empty when wrapper properties file is found"() {
        when:
        def result = subject.get()

        then:
        !result.present
    }
}
