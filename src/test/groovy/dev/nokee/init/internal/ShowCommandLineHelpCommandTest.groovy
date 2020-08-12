package dev.nokee.init.internal

import spock.lang.Specification
import spock.lang.Subject

@Subject(ShowCommandLineHelpCommand)
class ShowCommandLineHelpCommandTest extends Specification {
    def "shows proper command line help"() {
        given:
        def outStream = new ByteArrayOutputStream()
        def printer = new DefaultConsolePrinter(outStream)
        def subject = new ShowCommandLineHelpCommand(printer)

        when:
        subject.run()

        then:
        outStream.toString() == '''
USAGE: gradlew nokee [option...]

--help\t\tShows help message.
--version\t\tPrint version info.
--use-version\t\tSpecifies the nokee version to use in this project.

'''
    }
}
