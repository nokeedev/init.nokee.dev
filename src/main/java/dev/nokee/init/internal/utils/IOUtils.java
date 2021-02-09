package dev.nokee.init.internal.utils;

import java.io.Closeable;
import java.io.IOException;

public final class IOUtils {
	public static void closeQuietly(Closeable closeable) {
		try {
			closeable.close();
		} catch (IOException e) {
			// ignore
		}
	}
}
