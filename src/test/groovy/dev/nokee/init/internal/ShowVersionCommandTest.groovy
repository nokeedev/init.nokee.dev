package dev.nokee.init.internal

import org.gradle.util.VersionNumber
import spock.lang.Specification
import spock.lang.Subject

@Subject(ShowVersionCommand)
class ShowVersionCommandTest extends Specification {
    def "shows proper command line version"() {
        given:
        def outStream = new ByteArrayOutputStream()
        def printer = new DefaultConsolePrinter(outStream)
        def nokeeVersionProvider = Mock(NokeeVersionProvider)
        def subject = new ShowVersionCommand(printer, nokeeVersionProvider)

        when:
        nokeeVersionProvider.get() >> Optional.of(VersionNumber.parse("1.2.3-deadbeef"))
        subject.run()

        then:
        outStream.toString() == '''Using Nokee 1.2.3-deadbeef
'''
    }

    def "shows proper command line version when no version is found"() {
        given:
        def outStream = new ByteArrayOutputStream()
        def printer = new DefaultConsolePrinter(outStream)
        def nokeeVersionProvider = Mock(NokeeVersionProvider)
        def subject = new ShowVersionCommand(printer, nokeeVersionProvider)

        when:
        nokeeVersionProvider.get() >> Optional.empty()
        subject.run()

        then:
        outStream.toString() == '''Nokee isn't configured for this project, please use ./gradlew nokee --use-version=<version>
'''
    }
}
