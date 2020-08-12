package dev.nokee.init.internal;

import org.gradle.api.initialization.Settings;

public interface NokeeVersionProviderFactory {
    NokeeVersionProvider create(Settings settings);
}
