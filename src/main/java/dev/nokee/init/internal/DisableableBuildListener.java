package dev.nokee.init.internal;

import org.gradle.BuildAdapter;
import org.gradle.BuildListener;
import org.gradle.BuildResult;
import org.gradle.api.initialization.Settings;
import org.gradle.api.invocation.Gradle;

public final class DisableableBuildListener extends BuildAdapter {
	private final BuildListener delegate;
	private boolean disabled = false;

	public DisableableBuildListener(BuildListener delegate) {
		this.delegate = delegate;
	}

	public void disable() {
		disabled = true;
	}

	@Override
	public void beforeSettings(Settings settings) {
		if (!disabled) {
			delegate.beforeSettings(settings);
		}
	}

	@Override
	public void settingsEvaluated(Settings settings) {
		if (!disabled) {
			delegate.settingsEvaluated(settings);
		}
	}

	@Override
	public void projectsLoaded(Gradle gradle) {
		if (!disabled) {
			delegate.projectsLoaded(gradle);
		}
	}

	@Override
	public void projectsEvaluated(Gradle gradle) {
		if (!disabled) {
			delegate.projectsEvaluated(gradle);
		}
	}

	@Override
	public void buildFinished(BuildResult result) {
		if (!disabled) {
			delegate.buildFinished(result);
		}
	}
}
