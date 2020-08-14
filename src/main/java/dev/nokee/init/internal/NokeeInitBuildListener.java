package dev.nokee.init.internal;

import dev.nokee.init.internal.utils.GradleUtils;
import dev.nokee.init.internal.versions.NokeeVersionProviderFactory;
import org.gradle.BuildAdapter;
import org.gradle.api.initialization.Settings;
import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;

public final class NokeeInitBuildListener extends BuildAdapter {
    private static final Logger LOGGER = Logging.getLogger(NokeeInitBuildListener.class);
    private final NokeeVersionProviderFactory nokeeVersionProviderFactory;

    public NokeeInitBuildListener(NokeeVersionProviderFactory nokeeVersionProviderFactory) {
        this.nokeeVersionProviderFactory = nokeeVersionProviderFactory;
    }

    @Override
    public void beforeSettings(Settings settings) {
        nokeeVersionProviderFactory.create(settings).get().ifPresent(nokeeVersion -> {
            LOGGER.lifecycle("Build '" + GradleUtils.getIdentityPath(settings.getGradle()) + "' use Nokee version '" + nokeeVersion.toString() + "'.");
            settings.pluginManagement(new ConfigurePluginManagementAction(nokeeVersion));
        });
    }
}
