package dev.nokee.init.internal.utils;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public final class IOUtils {
	public static void closeQuietly(Closeable closeable) {
		try {
			closeable.close();
		} catch (IOException e) {
			// ignore
		}
	}

	private static final int EOF = -1;
	private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;
	public static int copy(InputStream input, OutputStream output) throws IOException {
		// copyLarge
		byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
		long count = 0;
		int n = 0;
		while (EOF != (n = input.read(buffer))) {
			output.write(buffer, 0, n);
			count += n;
		}

		// copy
		if (count > Integer.MAX_VALUE) {
			return -1;
		}
		return (int) count;
	}
}
