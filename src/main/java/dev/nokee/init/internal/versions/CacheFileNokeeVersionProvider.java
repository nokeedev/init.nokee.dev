package dev.nokee.init.internal.versions;

import lombok.val;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Optional;

public final class CacheFileNokeeVersionProvider implements NokeeVersionProvider {
	private final File cacheFile;

	public CacheFileNokeeVersionProvider(File cacheFile) {
		this.cacheFile = cacheFile;
	}

	@Override
	public Optional<NokeeVersion> get() {
		try (val reader = new NokeeVersionReader(new FileInputStream(cacheFile))) {
			return Optional.of(reader.read());
		} catch (IOException ex) {
			return Optional.empty();
		}
	}
}
