package dev.nokee.init.internal.versions;

import lombok.val;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

class NokeeVersionWriterTest {
	@Test
	void canWriteNokeeVersion() throws IOException {
		val content = new ByteArrayOutputStream();
		try (val writer = new NokeeVersionWriter(content)) {
			writer.write(NokeeVersion.parse("0.4.0"));
		}
		assertThat(content.toString(), equalTo("0.4.0"));
	}
}
