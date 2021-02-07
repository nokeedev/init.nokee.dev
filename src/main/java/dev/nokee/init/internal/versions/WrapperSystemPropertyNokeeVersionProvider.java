package dev.nokee.init.internal.versions;

import dev.nokee.init.internal.accessors.SystemPropertyAccessor;
import org.gradle.util.VersionNumber;

import java.util.Optional;

public final class WrapperSystemPropertyNokeeVersionProvider implements NokeeVersionProvider {
	private final SystemPropertyAccessor accessor;

	public WrapperSystemPropertyNokeeVersionProvider(SystemPropertyAccessor accessor) {
		this.accessor = accessor;
	}


	@Override
	public Optional<VersionNumber> get() {
		return Optional.ofNullable(accessor.get("useNokeeVersionFromWrapper")).map(VersionNumber::parse);
	}
}
