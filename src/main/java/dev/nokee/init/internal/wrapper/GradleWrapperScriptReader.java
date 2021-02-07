package dev.nokee.init.internal.wrapper;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public final class GradleWrapperScriptReader implements Closeable {
	private final Reader reader;

	public GradleWrapperScriptReader(InputStream in) {
		this.reader = new InputStreamReader(in, StandardCharsets.UTF_8);
	}

	public GradleWrapperScript read() {
		return new GradleWrapperScript(toString(reader));
	}

	private static String toString(Reader input) {
		return new Scanner(input).useDelimiter("\\A").next();
	}

	@Override
	public void close() throws IOException {
		reader.close();
	}
}
