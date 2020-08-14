package dev.nokee.init.internal.versions;

import org.gradle.util.VersionNumber;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Properties;
import java.util.function.Supplier;

public final class NonRelocatedWrapperPropertiesNokeeVersionProvider implements NokeeVersionProvider {
    private final Supplier<File> projectDirectory;

    public NonRelocatedWrapperPropertiesNokeeVersionProvider(Supplier<File> projectDirectory) {
        this.projectDirectory = projectDirectory;
    }

    @Override
    public Optional<VersionNumber> get() {
        try (InputStream inStream = new FileInputStream(new File(projectDirectory.get(), "gradle/wrapper/gradle-wrapper.properties"))) {
            Properties properties = new Properties();
            properties.load(inStream);
            return Optional.ofNullable(properties.getProperty("useNokeeVersion")).map(VersionNumber::parse);
        } catch (IOException e) {
            return Optional.empty();
        }
    }
}
