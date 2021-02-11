package dev.nokee.init;

import org.gradle.api.invocation.Gradle;
import org.gradle.testfixtures.ProjectBuilder;
import org.gradle.util.GradleVersion;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

class NokeeInitPluginTest {
	private final Gradle gradle = Mockito.spy(ProjectBuilder.builder().build().getGradle());

	@Test
	void doNothingOnUnsupportedVersion() {
		new NokeeInitPlugin(() -> GradleVersion.version("6.0")).apply(gradle);
		verifyNoInteractions(gradle);
	}

	@Test
	void configuresGradleOnSupportedVersion() {
		new NokeeInitPlugin(() -> GradleVersion.version("6.3")).apply(gradle);
		verify(gradle).addBuildListener(any());
	}
}
