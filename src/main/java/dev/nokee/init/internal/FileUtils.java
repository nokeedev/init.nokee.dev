package dev.nokee.init.internal;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Scanner;

public final class FileUtils {
    private FileUtils() {}

    public static String readFileToString(File file, Charset encoding) throws IOException {
        try (Scanner scanner = new Scanner(file, encoding.name())) {
            return scanner.useDelimiter("\\A").next();
        }
    }
}
