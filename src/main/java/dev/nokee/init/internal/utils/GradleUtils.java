package dev.nokee.init.internal.utils;

import org.gradle.api.initialization.Settings;
import org.gradle.api.internal.GradleInternal;
import org.gradle.api.invocation.Gradle;
import org.gradle.util.Path;

import java.io.File;

public final class GradleUtils {
	private GradleUtils() {}

	// TODO(grava): Move to grava-utils
	public static File getRootBuildDirectory(Settings self) {
		Gradle rootGradle = self.getGradle();
		while (rootGradle.getParent() != null) {
			rootGradle = rootGradle.getParent();
		}
		// We can't call getSettings() for the current Gradle object
		if (rootGradle == self.getGradle()) {
			return self.getRootDir();
		}
		return ((GradleInternal)rootGradle).getSettings().getRootDir();
	}

	// TODO(grava): Move to grava-utils
	public static Path getIdentityPath(Gradle self) {
		return ((GradleInternal)self).getIdentityPath();
	}
}
