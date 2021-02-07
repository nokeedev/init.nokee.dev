package dev.nokee.init.internal.versions;

import dev.nokee.init.internal.accessors.SystemPropertyAccessor;
import org.gradle.util.VersionNumber;

import java.util.Optional;

public final class DefaultSystemPropertyNokeeVersionProvider implements NokeeVersionProvider {
	private final SystemPropertyAccessor accessor;

	public DefaultSystemPropertyNokeeVersionProvider(SystemPropertyAccessor accessor) {
		this.accessor = accessor;
	}

	@Override
	public Optional<VersionNumber> get() {
		return Optional.ofNullable(accessor.get("use-nokee-version")).map(VersionNumber::parse);
	}
}
