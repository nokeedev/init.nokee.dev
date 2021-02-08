package dev.nokee.init.internal.versions;

public final class NokeeVersionFactory {
	private final NokeeVersionSource versionSource;

	public NokeeVersionFactory(NokeeVersionSource versionSource) {
		this.versionSource = versionSource;
	}

	public NokeeVersion create(String version) {
		return new NokeeVersion(version, versionSource);
	}
}
