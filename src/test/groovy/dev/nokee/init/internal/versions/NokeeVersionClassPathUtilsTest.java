package dev.nokee.init.internal.versions;

import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.net.URL;

import static com.spotify.hamcrest.optional.OptionalMatchers.emptyOptional;
import static com.spotify.hamcrest.optional.OptionalMatchers.optionalWithValue;
import static dev.nokee.init.internal.versions.NokeeVersionClassPathUtils.inferVersion;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class NokeeVersionClassPathUtilsTest {
	@Test
	void canInferNokeeVersionFromClassPathWithReleaseArtifacts() throws MalformedURLException {
		assertThat(inferVersion(url("file:/private/var/folders/hx/ll6hjrq12rx9j37_v4696_7h0000gn/T/.gradle-test-kit-daniel/caches/modules-2/files-2.1/dev.nokee/platformJni/0.4.0/c059174d43c9a88c6bda735213acc17183cac26c/platformJni-0.4.0.jar")),
			optionalWithValue(equalTo("0.4.0")));
	}

	@Test
	void canInferNokeeVersionFromClassPathWithNightlyArtifacts() throws MalformedURLException {
		assertThat(inferVersion(url("file:/private/var/folders/hx/ll6hjrq12rx9j37_v4696_7h0000gn/T/.gradle-test-kit-daniel/caches/modules-2/files-2.1/dev.nokee/platformJni/0.5.0-12c22234/c059174d43c9a88c6bda735213acc17183cac26c/platformJni-0.5.0-12c22234.jar")),
			optionalWithValue(equalTo("0.5.0-12c22234")));
	}

	@Test
	void canInferNokeeVersionFromClassPathInJars8Caches() throws MalformedURLException {
		assertThat(inferVersion(url("file:/C:/Users/Danie/.gradle/caches/jars-8/9a7accb65ab55b61b028f3d2be639d1c/platformJni-0.5.0.jar")),
			optionalWithValue(equalTo("0.5.0")));
	}

	@Test
	void cannotInferNokeeVersionFromClassPathWithoutNokeeArtifacts() throws MalformedURLException {
		assertThat(inferVersion(url("file:/Users/daniel/.gradle/wrapper/dists/gradle-6.2.1-bin/5m14x4d33sfxbtkeo25s43l8q/gradle-6.2.1/lib/failureaccess-1.0.1.jar")),
			emptyOptional());
	}

	private static URL url(String url) throws MalformedURLException {
		return new URL(url);
	}
}
