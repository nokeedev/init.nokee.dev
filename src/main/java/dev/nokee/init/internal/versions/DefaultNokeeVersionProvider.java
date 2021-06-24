package dev.nokee.init.internal.versions;

import lombok.val;
import lombok.var;
import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.function.Function;

import static dev.nokee.init.internal.versions.DeprecatedNokeeVersionProvider.warnIfPresent;

// TODO: Not the right name
public final class DefaultNokeeVersionProvider implements Callable<NokeeVersion> {
	private static final Logger LOGGER = Logging.getLogger(DefaultNokeeVersionProvider.class);
	private final List<NokeeVersionProvider> versionProviders = new ArrayList<>();

	public DefaultNokeeVersionProvider(NokeeVersionProviderFactory providerFactory) {
		// Gradle wrapper properties
		versionProviders.add(warnIfPresent(providerFactory.gradleWrapperProperty("useNokeeVersion")));
		versionProviders.add(providerFactory.gradleWrapperProperty("nokeeVersion"));

		// Cache file
		versionProviders.add(warnIfPresent(providerFactory.cacheFile(".gradle/use-nokee-version.txt")));
		versionProviders.add(providerFactory.cacheFile(".gradle/nokee-version.txt"));

		// Environment variables
		versionProviders.add(warnIfPresent(providerFactory.environmentVariable("USE_NOKEE_VERSION")));
		versionProviders.add(providerFactory.environmentVariable("NOKEE_VERSION"));

		// Gradle properties
		versionProviders.add(warnIfPresent(providerFactory.gradleProperty("use-nokee-version")));
		versionProviders.add(providerFactory.gradleProperty("nokee-version"));

		// System properties
		versionProviders.add(warnIfPresent(providerFactory.systemProperty("useNokeeVersionFromWrapper"), "Regenerate your wrapper ./gradlew wrapper."));
		versionProviders.add(warnIfPresent(providerFactory.systemProperty("use-nokee-version")));
		versionProviders.add(providerFactory.systemProperty("nokee-version"));

		// TODO: This provider should be added after settings is evaluated as it cause issue with version alignments
		// Buildscript block (most impactful, we should follow)
//		versionProviders.add(providerFactory.buildscript());
	}

	// TODO: More like warn overload
	private static Function<NokeeVersion, NokeeVersion> merge(NokeeVersion version) {
		return v -> {
			if (!v.equals(version)) {
				LOGGER.warn("WARNING: " + version + " overrides " + v + ".");
			}
			return version;
		};
	}

	@Nullable
	@Override
	public NokeeVersion call() throws Exception {
		var result = Optional.<NokeeVersion>empty();
		for (NokeeVersionProvider versionProvider : versionProviders) {
			val version = versionProvider.get();
			if (result.isPresent()) {
				if (version.isPresent()) {
					result = result.map(merge(version.get()));
				}
			} else {
				result = version;
			}
		}
		return result.orElse(null);
	}
}
