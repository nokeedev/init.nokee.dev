package dev.nokee.init.internal.versions;

import dev.nokee.init.internal.utils.FileUtils;
import org.gradle.util.VersionNumber;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.function.Supplier;

public final class CacheFileNokeeVersionProvider implements NokeeVersionProvider {
	private static final NokeeVersionFactory FACTORY = new NokeeVersionFactory(NokeeVersionSource.CacheFile);
	private final File cacheFile;

	public CacheFileNokeeVersionProvider(File cacheFile) {
		this.cacheFile = cacheFile;
	}

	@Override
	public Optional<NokeeVersion> get() {
//		val d = (URLClassLoader) getClass().getClassLoader();
//		Arrays.stream(d.getURLs()).forEach(System.out::println);
//		Arrays.stream(((URLClassLoader)d.getParent()).getURLs()).forEach(System.out::println);
		try {
			String content = FileUtils.readFileToString(cacheFile, StandardCharsets.UTF_8).trim();
			return Optional.of(FACTORY.create(content));
		} catch (IOException ex) {
			return Optional.empty();
		}
	}
}
