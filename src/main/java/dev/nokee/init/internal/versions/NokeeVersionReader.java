package dev.nokee.init.internal.versions;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public final class NokeeVersionReader implements Closeable {
	private final Reader reader;

	public NokeeVersionReader(InputStream in) {
		this.reader = new InputStreamReader(in, StandardCharsets.UTF_8);
	}

	public NokeeVersion read() {
		return new NokeeVersion(toString(reader).trim(), NokeeVersionSource.CacheFile);
	}

	private static String toString(Reader input) {
		return new Scanner(input).useDelimiter("\\A").next();
	}

	@Override
	public void close() throws IOException {
		reader.close();
	}
}
