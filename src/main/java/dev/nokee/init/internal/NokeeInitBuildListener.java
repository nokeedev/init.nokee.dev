package dev.nokee.init.internal;

import lombok.val;
import org.gradle.BuildAdapter;
import org.gradle.api.Action;
import org.gradle.api.Project;
import org.gradle.api.initialization.Settings;
import org.gradle.plugin.management.PluginManagementSpec;

import static dev.nokee.init.internal.NokeeRepositories.releaseRepository;
import static dev.nokee.init.internal.NokeeRepositories.snapshotRepository;

public final class NokeeInitBuildListener extends BuildAdapter {
	private static final String NOKEE_EXTENSION_NAME = "nokee";

	@Override
	public void beforeSettings(Settings settings) {
		val extension = registerExtension(settings);
		settings.pluginManagement(configurePluginResolution(extension));
	}

	private static Action<PluginManagementSpec> configurePluginResolution(NokeeExtension extension) {
		return spec -> {
			spec.getRepositories().maven(releaseRepository());
			spec.getRepositories().maven(snapshotRepository());
			spec.getRepositories().gradlePluginPortal();
			spec.resolutionStrategy(strategy -> {
				strategy.eachPlugin(details -> {
					if (details.getRequested().getId().getId().startsWith("dev.nokee.") && extension.getVersion().isPresent()) {
						details.useVersion(extension.getVersion().get().get().toString());
					}
				});
			});
		};
	}

	private static NokeeExtension registerExtension(Settings settings) {
		val extension = settings.getExtensions().create(NOKEE_EXTENSION_NAME, NokeeExtension.class, settings);
		settings.getGradle().rootProject(mountExtension(extension));
		return extension;
	}

	private static Action<Project> mountExtension(NokeeExtension extension) {
		return project -> project.getExtensions().add(NOKEE_EXTENSION_NAME, extension);
	}
}
