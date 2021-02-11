package dev.nokee.init.internal.versions;

import lombok.SneakyThrows;
import lombok.val;
import org.gradle.internal.logging.events.LogEvent;
import org.gradle.internal.logging.events.OutputEvent;
import org.gradle.internal.logging.events.OutputEventListener;
import org.gradle.internal.logging.slf4j.OutputEventListenerBackedLoggerContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import static dev.nokee.init.internal.versions.DeprecatedNokeeVersionProvider.warnIfPresent;
import static dev.nokee.init.internal.versions.NokeeVersionSource.*;
import static java.util.Optional.of;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.hasItemInArray;

class DeprecatedNokeeVersionProviderTest {
	private OutputEventListener listener;

	@BeforeEach
	void forwardLoggerFactoryToStdOut() {
		val loggerFactory = (OutputEventListenerBackedLoggerContext) LoggerFactory.getILoggerFactory();
		listener = loggerFactory.getOutputEventListener();
		loggerFactory.setOutputEventListener(new OutputEventListener() {
			@Override
			@SneakyThrows
			public void onOutput(OutputEvent event) {
				if (event instanceof LogEvent) {
					System.out.print(((LogEvent) event).getMessage());
				}
			}
		});
	}

	@AfterEach
	void resetLoggerFactory() {
		val loggerFactory = (OutputEventListenerBackedLoggerContext) LoggerFactory.getILoggerFactory();
		loggerFactory.setOutputEventListener(listener);
	}

	@Test
	@StdIo
	void warnsOnDeprecatedEnvironmentVariable(StdOut output) {
		warnIfPresent(() -> of(new NokeeVersion("0.5.0", EnvironmentVariable))).get().get();
		assertThat(output.capturedLines(), hasItemInArray("WARNING: Use NOKEE_VERSION environment variable instead."));
	}

	@Test
	@StdIo
	void warnsOnDeprecatedGradleWrapperProperty(StdOut output) {
		warnIfPresent(() -> of(new NokeeVersion("0.5.0", GradleWrapperProperty))).get().get();
		assertThat(output.capturedLines(), hasItemInArray("WARNING: Use nokeeVersion Gradle wrapper property instead."));
	}

	@Test
	@StdIo
	void warnsOnDeprecatedCacheFile(StdOut output) {
		warnIfPresent(() -> of(new NokeeVersion("0.5.0", CacheFile))).get().get();
		assertThat(output.capturedLines(), hasItemInArray("WARNING: Use .gradle/nokee-version.txt cache file instead."));
	}

	@Test
	@StdIo
	void warnsOnDeprecatedSystemProperty(StdOut output) {
		warnIfPresent(() -> of(new NokeeVersion("0.5.0", SystemProperty))).get().get();
		assertThat(output.capturedLines(), hasItemInArray("WARNING: Use nokee-version System property instead."));
	}

	@Test
	@StdIo
	void warnsOnDeprecatedGradleProperty(StdOut output) {
		warnIfPresent(() -> of(new NokeeVersion("0.5.0", GradleProperty))).get().get();
		assertThat(output.capturedLines(), hasItemInArray("WARNING: Use nokee-version Gradle property instead."));
	}

	@Test
	@StdIo
	void doesNotWarnWhenVersionIsAbsent(StdOut output) {
		warnIfPresent(Optional::empty).get().isPresent();
		assertThat(String.join("\n", output.capturedLines()), emptyString());
	}

	@Test
	@StdIo
	void doesNotWarnCustomMessageWhenVersionIsAbsent(StdOut output) {
		warnIfPresent(Optional::empty, "Custom message").get().isPresent();
		System.err.println(output.capturedLines()[0]);
		assertThat(String.join("\n", output.capturedLines()), emptyString());
	}

	@Test
	@StdIo
	void canWarnCustomMessageWhenVersionIsPresent(StdOut output) {
		warnIfPresent(() -> of(new NokeeVersion("0.5.0", None)), "Custom message.").get().isPresent();
		assertThat(output.capturedLines(), hasItemInArray("WARNING: Custom message."));
	}
}
