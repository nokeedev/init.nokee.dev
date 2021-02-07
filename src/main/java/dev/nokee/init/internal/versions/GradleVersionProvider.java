package dev.nokee.init.internal.versions;

import org.gradle.util.GradleVersion;

public interface GradleVersionProvider {
	GradleVersion get();
}
