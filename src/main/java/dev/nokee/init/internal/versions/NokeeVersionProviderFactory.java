package dev.nokee.init.internal.versions;

import org.gradle.api.initialization.Settings;

public interface NokeeVersionProviderFactory {
	NokeeVersionProvider create(Settings settings);
}
