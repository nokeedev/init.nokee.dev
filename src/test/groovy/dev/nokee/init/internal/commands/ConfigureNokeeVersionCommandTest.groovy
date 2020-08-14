package dev.nokee.init.internal.commands


import dev.nokee.init.internal.NokeeVersionWriter
import spock.lang.Specification
import spock.lang.Subject

@Subject(ConfigureNokeeVersionCommand)
class ConfigureNokeeVersionCommandTest extends Specification {
    def "writes specified version when present"() {
        given:
        def writer = Mock(NokeeVersionWriter)
        def subject = new ConfigureNokeeVersionCommand({ '1.2.3' }, writer)

        when:
        subject.run()

        then:
        1 * writer.write(_) >> { args ->
            assert args[0].major == 1
            assert args[0].minor == 2
            assert args[0].micro == 3
            assert args[0].qualifier == null
        }
    }
}
