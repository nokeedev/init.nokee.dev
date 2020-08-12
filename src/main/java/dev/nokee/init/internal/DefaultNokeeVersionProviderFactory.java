package dev.nokee.init.internal;

import org.gradle.api.initialization.Settings;

public class DefaultNokeeVersionProviderFactory implements NokeeVersionProviderFactory {
    @Override
    public NokeeVersionProvider create(Settings settings) {
        return new ProjectNokeeVersionProvider(() -> GradleUtils.getRootBuildDirectory(settings));
    }
}
