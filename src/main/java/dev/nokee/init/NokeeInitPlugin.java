package dev.nokee.init;

import dev.nokee.init.internal.DisableAware;
import dev.nokee.init.internal.DisableAwareBuildListener;
import dev.nokee.init.internal.NokeeInitBuildListener;
import dev.nokee.init.internal.versions.DefaultGradleVersionProvider;
import dev.nokee.init.internal.versions.GradleVersionProvider;
import lombok.val;
import org.gradle.api.Plugin;
import org.gradle.api.invocation.Gradle;
import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;
import org.gradle.util.GradleVersion;

import javax.inject.Inject;

import java.util.function.Predicate;

import static dev.nokee.init.internal.DuplicateNokeeInitPluginLoadsDetectionAction.disableDuplicateNokeeInitPluginLoads;
import static java.util.function.Predicate.isEqual;

/**
 * Nokee's deep integration plugin for Gradle.
 * The plugin serves as a global entrypoint to configure Gradle to use Nokee and to manage Nokee versions.
 */
public class NokeeInitPlugin implements Plugin<Gradle>, DisableAware {
	private static final Logger LOGGER = Logging.getLogger(NokeeInitPlugin.class);
	private static final GradleVersion MINIMUM_GRADLE_SUPPORTED = GradleVersion.version("6.2.1");
	private final GradleVersionProvider gradleVersionProvider;
	private DisableAware delegate = null;

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
			if (gradle.getPlugins().stream().anyMatch(isEqual(this).negate().and(isNokeeInitPlugin()))) {
				LOGGER.warn("WARNING: Another Nokee init plugin is already loaded, disabling duplicate plugin loading.");
				return;
			}
			val nokeeBuildListener = new DisableAwareBuildListener(new NokeeInitBuildListener());
			delegate = nokeeBuildListener;
			gradle.getPlugins().all(disableDuplicateNokeeInitPluginLoads());

			gradle.addBuildListener(nokeeBuildListener);
		}
	}

	private static Predicate<Object> isNokeeInitPlugin() {
		return it -> it.getClass().getSimpleName().equals("NokeeInitPlugin");
	}

	@Override
	public void disable() {
		if (delegate != null) {
			delegate.disable();
		}
	}

	public String getVersion() {
		return "0.8.3";
	}
}
