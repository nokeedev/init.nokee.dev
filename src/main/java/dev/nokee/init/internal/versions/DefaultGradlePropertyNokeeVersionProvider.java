package dev.nokee.init.internal.versions;

import dev.nokee.init.internal.NokeeVersionProvider;
import dev.nokee.init.internal.accessors.GradlePropertyAccessor;
import org.gradle.util.VersionNumber;

import java.util.Optional;

public final class DefaultGradlePropertyNokeeVersionProvider implements NokeeVersionProvider {
    private final GradlePropertyAccessor accessor;

    public DefaultGradlePropertyNokeeVersionProvider(GradlePropertyAccessor accessor) {
        this.accessor = accessor;
    }

    @Override
    public Optional<VersionNumber> get() {
        return Optional.ofNullable(accessor.get("use-nokee-version")).map(VersionNumber::parse);
    }
}
