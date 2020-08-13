package dev.nokee.init.internal;

import org.gradle.api.file.Directory;
import org.gradle.api.provider.Provider;
import org.gradle.util.VersionNumber;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Optional;
import java.util.function.Supplier;

public final class ProjectNokeeVersionProvider implements NokeeVersionProvider {
    private final Supplier<File> projectDirectory;

    public ProjectNokeeVersionProvider(Supplier<File> projectDirectory) {
        this.projectDirectory = projectDirectory;
    }

    @Override
    public Optional<VersionNumber> get() {
        try {
            String content = FileUtils.readFileToString(new File(projectDirectory.get(), ".gradle/use-nokee-version.txt"), Charset.defaultCharset()).trim();
            return Optional.of(VersionNumber.parse(content));
        } catch (IOException ex) {
            return Optional.empty();
        }
    }
}
