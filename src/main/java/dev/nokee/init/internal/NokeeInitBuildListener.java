package dev.nokee.init.internal;

import lombok.val;
import org.gradle.BuildAdapter;
import org.gradle.api.Action;
import org.gradle.api.Project;
import org.gradle.api.initialization.Settings;
import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;

public final class NokeeInitBuildListener extends BuildAdapter {
	private static final String NOKEE_EXTENSION_NAME = "nokee";
	private static final Logger LOGGER = Logging.getLogger(NokeeInitBuildListener.class);

	@Override
	public void beforeSettings(Settings settings) {
		val extension = registerExtension(settings);

//		settings.pluginManagement(spec -> {
//			System.out.println("LLL " + spec);
//			spec.resolutionStrategy(stra -> {
//				System.out.println("FFF " + stra);
//				stra.eachPlugin(de -> {
//					System.out.println("RRR " + de);
//				});
//			});
//		});
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
