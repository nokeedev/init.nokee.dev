package dev.nokee.init.internal.versions;

import org.gradle.util.VersionNumber;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public final class CompositeNokeeVersionProvider implements NokeeVersionProvider {
    private final List<NokeeVersionProvider> nokeeVersionProviders;

    public CompositeNokeeVersionProvider(NokeeVersionProvider... nokeeVersionProviders) {
        this.nokeeVersionProviders = Arrays.asList(nokeeVersionProviders);
    }

    @Override
    public Optional<VersionNumber> get() {
        for (NokeeVersionProvider nokeeVersionProvider : nokeeVersionProviders) {
            Optional<VersionNumber> result = nokeeVersionProvider.get();
            if (result.isPresent()) {
                return result;
            }
        }
        return Optional.empty();
    }
}
