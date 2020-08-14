package dev.nokee.init.internal;

import dev.nokee.init.internal.accessors.DefaultEnvironmentVariableAccessor;
import dev.nokee.init.internal.accessors.DefaultSystemPropertyAccessor;
import dev.nokee.init.internal.versions.DefaultNokeeVersionProvider;
import org.gradle.api.initialization.Settings;

public final class DefaultNokeeVersionProviderFactory implements NokeeVersionProviderFactory {
    @Override
    public NokeeVersionProvider create(Settings settings) {
        return new DefaultNokeeVersionProvider(() -> GradleUtils.getRootBuildDirectory(settings), new DefaultSystemPropertyAccessor(), new DefaultEnvironmentVariableAccessor());
    }
}
