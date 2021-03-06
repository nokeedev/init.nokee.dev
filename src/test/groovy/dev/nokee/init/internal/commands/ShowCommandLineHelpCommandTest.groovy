package dev.nokee.init.internal.commands

import dev.nokee.init.internal.DefaultConsolePrinter
import org.gradle.util.TextUtil
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
		TextUtil.normaliseLineSeparators(outStream.toString()) == '''
USAGE: gradlew nokee [option...]

--show-help\t\tShows help message.
--show-version\t\tPrint version info.
--use-version\t\tSpecifies the nokee version to use in this project.

'''
	}
}
