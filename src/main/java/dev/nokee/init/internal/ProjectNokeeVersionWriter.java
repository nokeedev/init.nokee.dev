package dev.nokee.init.internal;

import org.gradle.util.VersionNumber;

import java.io.*;
import java.nio.charset.Charset;
import java.util.function.Supplier;

public final class ProjectNokeeVersionWriter implements NokeeVersionWriter {
    private final Supplier<File> projectDirectory;

    public ProjectNokeeVersionWriter(Supplier<File> projectDirectory) {
        this.projectDirectory = projectDirectory;
    }

    @Override
    public void write(VersionNumber nokeeVersion) {
        File versionFile = new File(projectDirectory.get(), ".gradle/use-nokee-version.txt");
        versionFile.getParentFile().mkdirs();
        try (OutputStream outStream = new FileOutputStream(versionFile)) {
            outStream.write(nokeeVersion.toString().getBytes(Charset.defaultCharset()));
            outStream.flush();
        } catch (IOException e) {
            throw new UncheckedIOException(String.format("Could not write nokee Version '%s' in file '%s'.", nokeeVersion.toString(), versionFile.getAbsolutePath()), e);
        }
    }
}
