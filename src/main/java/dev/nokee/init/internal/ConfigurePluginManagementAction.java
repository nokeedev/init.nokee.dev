package dev.nokee.init.internal;

import org.gradle.api.Action;
import org.gradle.plugin.management.PluginManagementSpec;
import org.gradle.util.VersionNumber;

public final class ConfigurePluginManagementAction implements Action<PluginManagementSpec> {
    private final VersionNumber nokeeVersion;

    public ConfigurePluginManagementAction(VersionNumber nokeeVersion) {
        this.nokeeVersion = nokeeVersion;
    }

    @Override
    public void execute(PluginManagementSpec pluginManagement) {
        pluginManagement.repositories(repositories -> {
            String nokeeLocalRepository = System.getProperty("NOKEE_LOCAL_REPOSITORY", System.getenv("NOKEE_LOCAL_REPOSITORY"));
            if (nokeeLocalRepository != null) {
                repositories.maven(repo -> repo.setUrl(nokeeLocalRepository));
            }
            repositories.gradlePluginPortal();
            if (nokeeVersion.getQualifier() != null) {
                repositories.maven(repo -> repo.setUrl("https://dl.bintray.com/nokeedev/distributions-snapshots"));
            }
        });

        pluginManagement.getResolutionStrategy().eachPlugin(new AlignPluginNamespaceRequestToSpecificVersionAction("dev.nokee.", nokeeVersion));
    }
}
