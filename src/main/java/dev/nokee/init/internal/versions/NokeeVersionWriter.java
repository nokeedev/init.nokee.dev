package dev.nokee.init.internal.versions;

import dev.nokee.init.internal.wrapper.GradleWrapperScript;

import java.io.*;
import java.nio.charset.StandardCharsets;

public final class NokeeVersionWriter implements Closeable {
	private final Writer writer;

	public NokeeVersionWriter(OutputStream out) {
		this.writer = new OutputStreamWriter(out, StandardCharsets.UTF_8);
	}

	public void write(NokeeVersion version) throws IOException {
		writer.write(version.get().toString());
	}

	@Override
	public void close() throws IOException {
		writer.close();
	}
}
