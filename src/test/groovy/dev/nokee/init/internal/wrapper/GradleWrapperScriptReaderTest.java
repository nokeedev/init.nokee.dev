package dev.nokee.init.internal.wrapper;

import lombok.val;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static dev.nokee.init.internal.wrapper.GradleWrapperScriptTestUtils.script;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

class GradleWrapperScriptReaderTest {
	@Test
	void canReadGradleWrapperScript() throws IOException {
		try (val reader = new GradleWrapperScriptReader(new ByteArrayInputStream("some value".getBytes()))) {
			assertThat(reader.read(), equalTo(script("some value")));
		}
	}
}
