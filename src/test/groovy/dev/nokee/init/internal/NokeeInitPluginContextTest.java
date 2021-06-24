package dev.nokee.init.internal;

import lombok.val;
import org.gradle.api.Plugin;
import org.gradle.api.invocation.Gradle;
import org.junit.jupiter.api.Test;

import static org.gradle.util.VersionNumber.parse;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class NokeeInitPluginContextTest {
	@Test
	void canCheckIfPluginCanBeDisabled() {
		assertThat(new NokeeInitPluginContext(new CanDisablePlugin()).canDisable(), equalTo(true));
		assertThat(new NokeeInitPluginContext(new CannotDisablePlugin()).canDisable(), equalTo(false));
	}

	@Test
	void throwsExceptionWhenTryToDisableUndisablingPlugin() {
		assertThrows(RuntimeException.class, () -> new NokeeInitPluginContext(new CannotDisablePlugin()).disable());
	}

	@Test
	void canDisablePlugin() {
		val plugin = new CanDisablePlugin();
		assertDoesNotThrow(() -> new NokeeInitPluginContext(plugin).disable());
		assertThat(plugin.disabled(), equalTo(true));
	}

	@Test
	void canAccessPluginVersionWhenAvailable() {
		assertThat(new NokeeInitPluginContext(new CanDisablePlugin()).getVersion(), equalTo(parse("0.8.0")));
	}

	@Test
	void cannotAccessPluginVersionWhenUnavailable() {
		assertThat(new NokeeInitPluginContext(new CannotDisablePlugin()).getVersion(), equalTo(parse("0.0.0")));
	}

	private static final class CanDisablePlugin implements Plugin<Gradle> {
		private boolean disabled = false;
		@Override
		public void apply(Gradle target) {}

		public void disable() {
			disabled = true;
		}

		public boolean disabled() {
			return disabled;
		}

		public String getVersion() {
			return "0.8.0";
		}
	}

	private static final class CannotDisablePlugin implements Plugin<Gradle> {
		@Override
		public void apply(Gradle target) {}
	}
}
