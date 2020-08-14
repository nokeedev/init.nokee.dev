package dev.nokee.init.internal.versions;

import dev.nokee.init.internal.utils.GradleUtils;
import dev.nokee.init.internal.accessors.DefaultEnvironmentVariableAccessor;
import dev.nokee.init.internal.accessors.DefaultSystemPropertyAccessor;
import org.gradle.api.initialization.Settings;

public final class DefaultNokeeVersionProviderFactory implements NokeeVersionProviderFactory {
    @Override
    public NokeeVersionProvider create(Settings settings) {
        return new DefaultNokeeVersionProvider(() -> GradleUtils.getRootBuildDirectory(settings), DefaultSystemPropertyAccessor.INSTANCE, DefaultEnvironmentVariableAccessor.INSTANCE);
    }
}
