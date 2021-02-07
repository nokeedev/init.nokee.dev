package dev.nokee.init.internal;

import org.gradle.util.VersionNumber;

public interface NokeeVersionWriter {
	void write(VersionNumber nokeeVersion);
}
