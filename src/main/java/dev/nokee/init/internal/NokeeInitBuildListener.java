package dev.nokee.init.internal;

import dev.nokee.init.NokeeInitPlugin;
import dev.nokee.init.internal.wrapper.RegisterWrapperTaskEnhancementAction;
import lombok.val;
import org.gradle.BuildAdapter;
import org.gradle.api.Action;
import org.gradle.api.Project;
import org.gradle.api.initialization.Settings;
import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;
import org.gradle.plugin.management.PluginManagementSpec;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static dev.nokee.init.internal.NokeeRepositories.releaseRepository;
import static dev.nokee.init.internal.NokeeRepositories.snapshotRepository;

public final class NokeeInitBuildListener extends BuildAdapter {
	private static final String NOKEE_EXTENSION_NAME = "nokee";

	@Override
	public void beforeSettings(Settings settings) {
		settings.getGradle().getStartParameter().getAllInitScripts().forEach(this::warnIfNokeeInitScriptUsingBintray);
		val extension = registerExtension(settings);
		settings.pluginManagement(configurePluginResolution(extension));
		settings.getGradle().rootProject(new RegisterNokeeTaskAction());
		settings.getGradle().rootProject(new RegisterWrapperTaskEnhancementAction());
	}

	private static final Logger LOGGER = Logging.getLogger(NokeeInitPlugin.class);
	private static final Set<BintrayMarker> BINTRAY_MARKER = new HashSet<>(Arrays.asList(BintrayMarker.BintrayRepo, BintrayMarker.NokeeInit));
	private enum BintrayMarker { NokeeInit, BintrayRepo, None }
	private void warnIfNokeeInitScriptUsingBintray(File initScriptFile) {
		try (val reader = new BufferedReader(new FileReader(initScriptFile))) {
			val markers = reader.lines()
				.map(it -> {

					if (it.contains("init.nokee.dev")) {
						return BintrayMarker.NokeeInit;
					} else if (it.contains("dl.bintray.com/nokeedev")) {
						return BintrayMarker.BintrayRepo;
					}
					return BintrayMarker.None;
				})
				.filter(it -> !it.equals(BintrayMarker.None))
				.collect(Collectors.toSet());
			if (markers.equals(BINTRAY_MARKER)) {
				LOGGER.warn("Please update init script '" + initScriptFile.getAbsolutePath() + "' to a newer version.");
				LOGGER.warn("Learn more at https://github.com/nokeedev/init.nokee.dev#bintray-deprecation");
			}
		} catch (RuntimeException | IOException e) {
			// ignore all exceptions
		}
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
