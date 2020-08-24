package dev.nokee.init.internal.versions;

import org.gradle.util.VersionNumber;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Properties;

public final class WrapperPropertiesNokeeVersionProviderImpl implements NokeeVersionProvider {
    private final Properties properties = new Properties();

    public WrapperPropertiesNokeeVersionProviderImpl(File wrapperProperties) {
        try (InputStream inStream = new FileInputStream(wrapperProperties)) {
            properties.load(inStream);
        } catch (IOException e) {
            // Do nothing...
        }
    }

    @Override
    public Optional<VersionNumber> get() {
        return Optional.ofNullable(properties.getProperty("useNokeeVersion")).map(VersionNumber::parse);
    }
}
