package dev.nokee.init.internal.versions;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class NokeeVersionClassPathUtils {

	public static Optional<String> inferVersion(URL... classPath) {
		return inferVersion(Arrays.stream(classPath)
			.map(toUri())
			.map(File::new)
			.collect(Collectors.toSet()));
	}

	public static Optional<String> inferVersion(Set<File> classPath) {
		return classPath.stream()
			.filter(nokeeArtifacts())
			.map(File::getName)
			.map(extractVersion())
			.findFirst();
	}

	private static Predicate<File> nokeeArtifacts() {
		return it -> {
			// TODO: the cache seems to have changed, we should prepend all nokee artifact with nokee-
			// TODO: For now, let's have a list of known artifact baseName and find those
			//   Also add a flag to disable mismatch check (just in case), when the flag is enabled, we should warn on the command line the flag may hide version mismatch causing various errors at runtime, instead, report the error to Nokee project
			//
			return it.getName().startsWith("platformJni");
		};
	}

	private static Function<URL, URI> toUri() {
		return it -> {
			try {
				return it.toURI();
			} catch (URISyntaxException e) {
				throw new RuntimeException(e);
			}
		};
	}

	private static Function<String, String> extractVersion() {
		return it -> it.replace(".jar", "").replace("platformJni-", "");
	}
}
