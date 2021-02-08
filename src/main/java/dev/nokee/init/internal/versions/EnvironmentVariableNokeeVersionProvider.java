package dev.nokee.init.internal.versions;

import org.gradle.api.provider.Provider;
import org.gradle.api.provider.ProviderFactory;

import java.util.Optional;

import static dev.nokee.init.internal.utils.ProviderUtils.forUseAtConfigurationTime;

public final class EnvironmentVariableNokeeVersionProvider implements NokeeVersionProvider {
	private static final NokeeVersionFactory FACTORY = new NokeeVersionFactory(NokeeVersionSource.EnvironmentVariable);
	private final Provider<NokeeVersion> provider;

	public EnvironmentVariableNokeeVersionProvider(String variableName, ProviderFactory providers) {
		this.provider = forUseAtConfigurationTime(providers.environmentVariable(variableName)).map(FACTORY::create);
	}

	@Override
	public Optional<NokeeVersion> get() {
		return Optional.ofNullable(provider.getOrNull());
	}
}
