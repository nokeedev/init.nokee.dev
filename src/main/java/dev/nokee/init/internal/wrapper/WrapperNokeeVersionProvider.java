package dev.nokee.init.internal.wrapper;

import dev.nokee.init.internal.NokeeVersionProvider;
import org.gradle.api.provider.Provider;
import org.gradle.api.provider.ProviderFactory;
import org.gradle.util.VersionNumber;

import java.util.Optional;

public final class WrapperNokeeVersionProvider implements NokeeVersionProvider {

    private final Provider<String> nokeeVersion;
    private final ProviderFactory providers;

    public WrapperNokeeVersionProvider(Provider<String> nokeeVersion, ProviderFactory providers) {
        this.nokeeVersion = nokeeVersion;
        this.providers = providers;
    }

    @Override
    public Optional<VersionNumber> get() {
        return Optional.ofNullable(getUseNokeeVersionGradleProperty().orElse(getUseNokeeVersionSystemProperty()).orElse(nokeeVersion).getOrNull()).map(VersionNumber::parse);
    }

    Provider<String> getUseNokeeVersionSystemProperty() {
        return providers.systemProperty("use-nokee-version");
    }

    Provider<String> getUseNokeeVersionGradleProperty() {
        return providers.gradleProperty("use-nokee-version");
    }
}
