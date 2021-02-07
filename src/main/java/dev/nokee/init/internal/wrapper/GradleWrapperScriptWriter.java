package dev.nokee.init.internal.wrapper;

import java.io.*;
import java.nio.charset.StandardCharsets;

public final class GradleWrapperScriptWriter implements Closeable {
	private final Writer writer;

	public GradleWrapperScriptWriter(OutputStream out) {
		this.writer = new OutputStreamWriter(out, StandardCharsets.UTF_8);
	}

	public void write(GradleWrapperScript script) throws IOException {
		writer.write(script.get());
	}

	@Override
	public void close() throws IOException {
		writer.close();
	}
}
