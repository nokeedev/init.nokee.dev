package dev.nokee.init.internal.versions;

import java.util.Optional;

public interface NokeeVersionProvider {
	Optional<NokeeVersion> get();
}
