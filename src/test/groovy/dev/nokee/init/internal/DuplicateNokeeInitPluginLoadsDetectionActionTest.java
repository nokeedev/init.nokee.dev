package dev.nokee.init.internal;

import lombok.val;
import org.gradle.api.Action;
import org.gradle.api.Plugin;
import org.gradle.api.invocation.Gradle;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

class DuplicateNokeeInitPluginLoadsDetectionActionTest {
	private final DuplicateNokeeInitPluginLoadsDetectionAction.Callback callback = Mockito.mock(DuplicateNokeeInitPluginLoadsDetectionAction.Callback.class);
	private final Action<Plugin> subject = new DuplicateNokeeInitPluginLoadsDetectionAction(callback);

	@Test
	void doesNotTriggerCallbackWhenOnlyOnePluginIsLoaded() {
		subject.execute(new NokeeInitPlugin());
		verifyNoInteractions(callback);
	}

	@Test
	void triggerCallbackWhenTwoPluginIsLoaded() {
		val previousPlugin = new NokeeInitPlugin();
		val currentPlugin = new NokeeInitPlugin();
		subject.execute(previousPlugin);
		subject.execute(currentPlugin);
		verify(callback).onDuplicateLoads(new NokeeInitPluginContext(previousPlugin), new NokeeInitPluginContext(currentPlugin));
	}

	@Test
	void doesNotTriggerCallbackWhenPluginIsNotNokeeInitPlugin() {
		subject.execute(new UnrelatedPlugin());
		subject.execute(new NokeeInitPlugin());
		subject.execute(new UnrelatedPlugin());
		verifyNoInteractions(callback);
	}

	private final class UnrelatedPlugin implements Plugin<Gradle> {
		@Override
		public void apply(Gradle target) {}
	}

	private final class NokeeInitPlugin implements Plugin<Gradle> {
		@Override
		public void apply(Gradle target) {}
	}
}
