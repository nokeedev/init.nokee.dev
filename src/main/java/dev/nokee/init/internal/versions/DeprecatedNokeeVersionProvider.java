package dev.nokee.init.internal.versions;

import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;

import java.util.Optional;

public final class DeprecatedNokeeVersionProvider implements NokeeVersionProvider {
	private static final Logger LOGGER = Logging.getLogger(DeprecatedNokeeVersionProvider.class);
	private final NokeeVersionProvider delegate;
	private final String customDeprecationMessage;

	private DeprecatedNokeeVersionProvider(NokeeVersionProvider delegate, String customDeprecationMessage) {
		this.delegate = delegate;
		this.customDeprecationMessage = customDeprecationMessage;
	}

	public static NokeeVersionProvider warnIfPresent(NokeeVersionProvider delegate) {
		return new DeprecatedNokeeVersionProvider(delegate, null);
	}

	public static NokeeVersionProvider warnIfPresent(NokeeVersionProvider delegate, String customDeprecationMessage) {
		return new DeprecatedNokeeVersionProvider(delegate, customDeprecationMessage);
	}

	@Override
	public Optional<NokeeVersion> get() {
		return delegate.get().map(it -> {
			if (customDeprecationMessage == null) {
				String name = "nokee-version";
				if (it.getVersionSource().equals(NokeeVersionSource.EnvironmentVariable)) {
					name = "NOKEE_VERSION";
				} else if (it.getVersionSource().equals(NokeeVersionSource.CacheFile)) {
					name = ".gradle/nokee-version.txt";
				}
				LOGGER.warn("WARNING: Use " + name + " " + it.getVersionSource() + " instead.");
			} else {
				LOGGER.warn("WARNING: " + customDeprecationMessage);
			}
			return it;
		});
	}
}
