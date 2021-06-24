package dev.nokee.init.internal.versions;

import org.gradle.api.provider.Provider;
import org.gradle.api.provider.ProviderFactory;

import java.util.Optional;

import static dev.nokee.init.internal.utils.ProviderUtils.forUseAtConfigurationTime;

public final class SystemPropertyNokeeVersionProvider implements NokeeVersionProvider {
	private static final NokeeVersionFactory FACTORY = new NokeeVersionFactory(NokeeVersionSource.SystemProperty);
	private final Provider<NokeeVersion> provider;

	public SystemPropertyNokeeVersionProvider(String propertyName, ProviderFactory providers) {
		this.provider = forUseAtConfigurationTime(providers.systemProperty(propertyName)).map(FACTORY::create);
	}

	@Override
	public Optional<NokeeVersion> get() {
		return Optional.ofNullable(provider.getOrNull());
	}
}
