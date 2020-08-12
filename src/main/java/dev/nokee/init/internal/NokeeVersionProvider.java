package dev.nokee.init.internal;

import org.gradle.util.VersionNumber;

import java.util.Optional;

public interface NokeeVersionProvider {
    Optional<VersionNumber> get();
}
