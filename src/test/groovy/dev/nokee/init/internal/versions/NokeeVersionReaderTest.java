package dev.nokee.init.internal.versions;

import lombok.val;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

class NokeeVersionReaderTest {
	@Test
	void canReadNokeeVersion() throws IOException {
		try (val reader = new NokeeVersionReader(new ByteArrayInputStream("0.4.0".getBytes()))) {
			assertThat(reader.read(), equalTo(NokeeVersion.parse("0.4.0")));
		}
	}

	@Test
	void canTrimNewLines() throws IOException {
		try (val reader = new NokeeVersionReader(new ByteArrayInputStream("0.4.0\n\n".getBytes()))) {
			assertThat(reader.read(), equalTo(NokeeVersion.parse("0.4.0")));
		}
	}

	@Test
	void canTrimTrailingWhitespace() throws IOException {
		try (val reader = new NokeeVersionReader(new ByteArrayInputStream("0.4.0  ".getBytes()))) {
			assertThat(reader.read(), equalTo(NokeeVersion.parse("0.4.0")));
		}
	}
}
