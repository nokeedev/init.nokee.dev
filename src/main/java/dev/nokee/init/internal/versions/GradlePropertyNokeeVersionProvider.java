package dev.nokee.init.internal.versions;

import dev.nokee.init.internal.utils.ProviderUtils;
import org.gradle.api.provider.Provider;
import org.gradle.api.provider.ProviderFactory;
import org.gradle.util.VersionNumber;

import java.util.Optional;

import static dev.nokee.init.internal.utils.ProviderUtils.forUseAtConfigurationTime;

public final class GradlePropertyNokeeVersionProvider implements NokeeVersionProvider {
	private static final NokeeVersionFactory FACTORY = new NokeeVersionFactory(NokeeVersionSource.GradleProperty);
	private final Provider<NokeeVersion> provider;

	public GradlePropertyNokeeVersionProvider(String propertyName, ProviderFactory providers) {
		this.provider = forUseAtConfigurationTime(providers.gradleProperty(propertyName)).map(FACTORY::create);
	}

	@Override
	public Optional<NokeeVersion> get() {
		return Optional.ofNullable(provider.getOrNull());
	}
}
