package dev.nokee.init.internal.versions;

import dev.nokee.init.internal.accessors.EnvironmentVariableAccessor;
import dev.nokee.init.internal.NokeeVersionProvider;
import org.gradle.util.VersionNumber;

import java.util.Optional;

public final class EnvironmentVariableNokeeVersionProvider implements NokeeVersionProvider {
    private final EnvironmentVariableAccessor accessor;

    public EnvironmentVariableNokeeVersionProvider(EnvironmentVariableAccessor accessor) {
        this.accessor = accessor;
    }

    @Override
    public Optional<VersionNumber> get() {
        return Optional.ofNullable(accessor.get("USE_NOKEE_VERSION")).map(VersionNumber::parse);
    }
}
