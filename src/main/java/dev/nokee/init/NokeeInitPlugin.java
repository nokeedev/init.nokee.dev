package dev.nokee.init;

import dev.nokee.init.internal.DisableableBuildListener;
import dev.nokee.init.internal.NokeeInitBuildListener;
import dev.nokee.init.internal.RegisterNokeeTaskAction;
import dev.nokee.init.internal.versions.DefaultGradleVersionProvider;
import dev.nokee.init.internal.versions.GradleVersionProvider;
import dev.nokee.init.internal.wrapper.RegisterWrapperTaskEnhancementAction;
import org.gradle.api.Plugin;
import org.gradle.api.invocation.Gradle;
import org.gradle.util.GradleVersion;

import javax.inject.Inject;

public class NokeeInitPlugin implements Plugin<Gradle> {
	private static final GradleVersion MINIMUM_GRADLE_SUPPORTED = GradleVersion.version("6.2.1");
	private final GradleVersionProvider gradleVersionProvider;

	@Inject
	protected NokeeInitPlugin() {
		this(new DefaultGradleVersionProvider());
	}

	// for testing
	NokeeInitPlugin(GradleVersionProvider gradleVersionProvider) {
		this.gradleVersionProvider = gradleVersionProvider;
	}

	@Override
	public void apply(Gradle gradle) {
		if (MINIMUM_GRADLE_SUPPORTED.compareTo(gradleVersionProvider.get()) <= 0) {
			gradle.addBuildListener(new DisableableBuildListener(new NokeeInitBuildListener()));
		}
	}
}
