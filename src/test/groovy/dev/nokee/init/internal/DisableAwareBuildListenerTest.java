package dev.nokee.init.internal;

import lombok.val;
import org.gradle.BuildListener;
import org.gradle.BuildResult;
import org.gradle.api.initialization.Settings;
import org.gradle.api.invocation.Gradle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class DisableAwareBuildListenerTest {
	private final BuildListener delegate = mock(BuildListener.class);
	private final DisableAwareBuildListener subject = new DisableAwareBuildListener(delegate);

	@Nested
	class Enabled {
		@Test
		void delegatesCallToBeforeSettings() {
			val arg = mock(Settings.class);
			subject.beforeSettings(arg);
			verify(delegate).beforeSettings(arg);
		}

		@Test
		void delegatesCallToSettingsEvaluated() {
			val arg = mock(Settings.class);
			subject.settingsEvaluated(arg);
			verify(delegate).settingsEvaluated(arg);
		}

		@Test
		void delegatesCallToBuildFinished() {
			val arg = new BuildResult(null, null);
			subject.buildFinished(arg);
			verify(delegate).buildFinished(arg);
		}

		@Test
		void delegatesCallToProjectsEvaluated() {
			val arg = mock(Gradle.class);
			subject.projectsEvaluated(arg);
			verify(delegate).projectsEvaluated(arg);
		}

		@Test
		void delegatesCallToProjectsLoaded() {
			val arg = mock(Gradle.class);
			subject.projectsLoaded(arg);
			verify(delegate).projectsLoaded(arg);
		}
	}

	@Nested
	class Disabled {
		@BeforeEach
		void disableBuildListener() {
			subject.disable();
		}

		@Test
		void delegatesCallToBeforeSettings() {
			val arg = mock(Settings.class);
			subject.beforeSettings(arg);
			verify(delegate, never()).beforeSettings(arg);
		}

		@Test
		void delegatesCallToSettingsEvaluated() {
			val arg = mock(Settings.class);
			subject.settingsEvaluated(arg);
			verify(delegate, never()).settingsEvaluated(arg);
		}

		@Test
		void delegatesCallToBuildFinished() {
			val arg = new BuildResult(null, null);
			subject.buildFinished(arg);
			verify(delegate, never()).buildFinished(arg);
		}

		@Test
		void delegatesCallToProjectsEvaluated() {
			val arg = mock(Gradle.class);
			subject.projectsEvaluated(arg);
			verify(delegate, never()).projectsEvaluated(arg);
		}

		@Test
		void delegatesCallToProjectsLoaded() {
			val arg = mock(Gradle.class);
			subject.projectsLoaded(arg);
			verify(delegate, never()).projectsLoaded(arg);
		}
	}
}
