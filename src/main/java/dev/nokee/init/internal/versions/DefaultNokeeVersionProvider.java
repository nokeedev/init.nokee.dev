package dev.nokee.init.internal.versions;

import dev.nokee.init.internal.accessors.EnvironmentVariableAccessor;
import dev.nokee.init.internal.accessors.SystemPropertyAccessor;
import org.gradle.util.VersionNumber;

import java.io.File;
import java.util.Optional;
import java.util.function.Supplier;

public final class DefaultNokeeVersionProvider implements NokeeVersionProvider {
    private final NokeeVersionProvider delegate;

    public DefaultNokeeVersionProvider(Supplier<File> projectDirectory, SystemPropertyAccessor systemPropertyAccessor, EnvironmentVariableAccessor environmentVariableAccessor) {
        this.delegate = new CompositeNokeeVersionProvider(new DefaultSystemPropertyNokeeVersionProvider(systemPropertyAccessor), new EnvironmentVariableNokeeVersionProvider(environmentVariableAccessor), new CacheFileNokeeVersionProvider(projectDirectory), new WrapperSystemPropertyNokeeVersionProvider(systemPropertyAccessor), new NonRelocatedWrapperPropertiesNokeeVersionProvider(projectDirectory));
    }

    @Override
    public Optional<VersionNumber> get() {
        return delegate.get();
    }
}
