package dev.nokee.init.internal.utils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Scanner;

public final class FileUtils {
    private FileUtils() {}

    public static String readFileToString(File file, Charset encoding) throws IOException {
        try (Scanner scanner = new Scanner(file, encoding.name())) {
            return scanner.useDelimiter("\\A").next();
        }
    }

    public static void write(File file, CharSequence data, Charset encoding) throws IOException {
        try (OutputStream outStream = new FileOutputStream(file)) {
            outStream.write(data.toString().getBytes(encoding));
        }
    }
}
