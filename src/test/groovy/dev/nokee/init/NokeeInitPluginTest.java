package dev.nokee.init;

import dev.nokee.init.internal.NokeeInitBuildListener;
import org.gradle.api.invocation.Gradle;
import org.gradle.util.GradleVersion;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

class NokeeInitPluginTest {
	private final Gradle gradle = Mockito.mock(Gradle.class);

	@Test
	void doNothingOnUnsupportedVersion() {
		new NokeeInitPlugin(() -> GradleVersion.version("6.0")).apply(gradle);
		verifyNoInteractions(gradle);
	}

	@Test
	void configuresGradleOnSupportedVersion() {
		new NokeeInitPlugin(() -> GradleVersion.version("6.3")).apply(gradle);
		verify(gradle).addBuildListener(isA(NokeeInitBuildListener.class));
	}
}
