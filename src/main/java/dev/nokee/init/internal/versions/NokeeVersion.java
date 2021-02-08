package dev.nokee.init.internal.versions;

import lombok.EqualsAndHashCode;
import org.gradle.util.VersionNumber;

@EqualsAndHashCode
public final class NokeeVersion {
	private final String version;
	@EqualsAndHashCode.Exclude private final NokeeVersionSource versionSource;

	NokeeVersion(String version, NokeeVersionSource versionSource) {
		this.version = version;
		this.versionSource = versionSource;
	}

	public static NokeeVersion parse(String version) {
		return new NokeeVersion(version, NokeeVersionSource.None);
	}

	public VersionNumber get() {
		return VersionNumber.parse(version);
	}

	NokeeVersionSource getVersionSource() {
		return versionSource;
	}

	@Override
	public String toString() {
		return "version '" + version + "' (from " + versionSource + ")";
	}
}
