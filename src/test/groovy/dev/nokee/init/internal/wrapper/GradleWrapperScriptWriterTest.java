package dev.nokee.init.internal.wrapper;

import lombok.val;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static dev.nokee.init.internal.wrapper.GradleWrapperScriptTestUtils.script;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

class GradleWrapperScriptWriterTest {
	@Test
	void canWriteGradleWrapperScript() throws IOException {
		val content = new ByteArrayOutputStream();
		try (val writer = new GradleWrapperScriptWriter(content)) {
			writer.write(script("some value"));
		}
		assertThat(content.toString(), equalTo("some value"));
	}
}
