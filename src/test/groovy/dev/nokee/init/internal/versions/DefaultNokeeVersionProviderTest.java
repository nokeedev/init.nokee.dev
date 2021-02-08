package dev.nokee.init.internal.versions;

import lombok.SneakyThrows;
import lombok.val;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static java.util.Optional.of;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

class DefaultNokeeVersionProviderTest {
	private DefaultNokeeVersionProvider subject;
	private final NokeeVersionProvider nokeeVersionSystemPropertyProvider = Mockito.mock(NokeeVersionProvider.class);
	private final NokeeVersionProvider useNokeeVersionSystemPropertyProvider = Mockito.mock(NokeeVersionProvider.class);
	private final NokeeVersionProvider useNokeeVersionFromWrapperSystemPropertyProvider = Mockito.mock(NokeeVersionProvider.class);
	private final NokeeVersionProvider nokeeVersionGradlePropertyProvider = Mockito.mock(NokeeVersionProvider.class);
	private final NokeeVersionProvider useNokeeVersionGradlePropertyProvider = Mockito.mock(NokeeVersionProvider.class);
	private final NokeeVersionProvider nokeeVersionEnvironmentVariableProvider = Mockito.mock(NokeeVersionProvider.class);
	private final NokeeVersionProvider useNokeeVersionEnvironmentVariableProvider = Mockito.mock(NokeeVersionProvider.class);
	private final NokeeVersionProvider nokeeVersionCacheFileProvider = Mockito.mock(NokeeVersionProvider.class);
	private final NokeeVersionProvider useNokeeVersionCacheFileProvider = Mockito.mock(NokeeVersionProvider.class);
	private final NokeeVersionProvider buildscriptProvider = Mockito.mock(NokeeVersionProvider.class);
	private final NokeeVersionProvider nokeeVersionGradleWrapperPropertyProvider = Mockito.mock(NokeeVersionProvider.class);
	private final NokeeVersionProvider useNokeeVersionGradleWrapperPropertyProvider = Mockito.mock(NokeeVersionProvider.class);

	@BeforeEach
	void createSubject() {
		val factory = Mockito.mock(NokeeVersionProviderFactory.class);
		when(factory.gradleProperty("nokee-version")).thenReturn(nokeeVersionGradlePropertyProvider);
		when(factory.gradleProperty("use-nokee-version")).thenReturn(useNokeeVersionGradlePropertyProvider);
		when(factory.systemProperty("nokee-version")).thenReturn(nokeeVersionSystemPropertyProvider);
		when(factory.systemProperty("use-nokee-version")).thenReturn(useNokeeVersionSystemPropertyProvider);
		when(factory.systemProperty("useNokeeVersionFromWrapper")).thenReturn(useNokeeVersionFromWrapperSystemPropertyProvider);
		when(factory.environmentVariable("USE_NOKEE_VERSION")).thenReturn(useNokeeVersionEnvironmentVariableProvider);
		when(factory.environmentVariable("NOKEE_VERSION")).thenReturn(nokeeVersionEnvironmentVariableProvider);
		when(factory.cacheFile(".gradle/nokee-version.txt")).thenReturn(nokeeVersionCacheFileProvider);
		when(factory.cacheFile(".gradle/use-nokee-version.txt")).thenReturn(useNokeeVersionCacheFileProvider);
		when(factory.buildscript()).thenReturn(buildscriptProvider);
		when(factory.gradleWrapperProperty("nokeeVersion")).thenReturn(nokeeVersionGradleWrapperPropertyProvider);
		when(factory.gradleWrapperProperty("useNokeeVersion")).thenReturn(useNokeeVersionGradleWrapperPropertyProvider);
		subject = new DefaultNokeeVersionProvider(factory);

		when(nokeeVersionSystemPropertyProvider.get()).thenReturn(Optional.empty());
		when(useNokeeVersionSystemPropertyProvider.get()).thenReturn(Optional.empty());
		when(useNokeeVersionFromWrapperSystemPropertyProvider.get()).thenReturn(Optional.empty());
		when(nokeeVersionGradlePropertyProvider.get()).thenReturn(Optional.empty());
		when(useNokeeVersionGradlePropertyProvider.get()).thenReturn(Optional.empty());
		when(nokeeVersionEnvironmentVariableProvider.get()).thenReturn(Optional.empty());
		when(useNokeeVersionEnvironmentVariableProvider.get()).thenReturn(Optional.empty());
		when(nokeeVersionCacheFileProvider.get()).thenReturn(Optional.empty());
		when(useNokeeVersionCacheFileProvider.get()).thenReturn(Optional.empty());
		when(buildscriptProvider.get()).thenReturn(Optional.empty());
		when(nokeeVersionGradleWrapperPropertyProvider.get()).thenReturn(Optional.empty());
		when(useNokeeVersionGradleWrapperPropertyProvider.get()).thenReturn(Optional.empty());
	}

	private static NokeeVersion version(String v) {
		return NokeeVersion.parse(v);
	}

	//region Ordering of buildscript
	@Test
	void buildscriptOverwritesSystemProperty() {
		assertThat(buildscriptProvider, overrides(nokeeVersionSystemPropertyProvider));
	}

	@Test
	void buildscriptOverwritesGradleProperty() {
		assertThat(buildscriptProvider, overrides(nokeeVersionGradlePropertyProvider));
	}

	@Test
	void buildscriptOverwritesEnvironmentVariable() {
		assertThat(buildscriptProvider, overrides(nokeeVersionEnvironmentVariableProvider));
	}

	@Test
	void buildscriptOverwritesCacheFile() {
		assertThat(buildscriptProvider, overrides(nokeeVersionCacheFileProvider));
	}

	@Test
	void buildscriptOverwritesGradleWrapperProperty() {
		assertThat(buildscriptProvider, overrides(nokeeVersionGradleWrapperPropertyProvider));
	}
	//endregion

	//region Ordering of System properties
	@Test
	void systemPropertyOverwritesOldSystemProperty() {
		assertThat(nokeeVersionSystemPropertyProvider, overrides(useNokeeVersionSystemPropertyProvider));
	}

	@Test
	void oldSystemPropertyOverwritesGradleWrapperSystemProperty() {
		assertThat(useNokeeVersionSystemPropertyProvider, overrides(useNokeeVersionFromWrapperSystemPropertyProvider));
	}

	@Test
	void systemPropertyOverwritesGradleWrapperSystemProperty() {
		assertThat(nokeeVersionSystemPropertyProvider, overrides(useNokeeVersionFromWrapperSystemPropertyProvider));
	}

	@Test
	void systemPropertyOverwritesGradleProperty() {
		assertThat(nokeeVersionSystemPropertyProvider, overrides(nokeeVersionGradlePropertyProvider));
	}

	@Test
	void systemPropertyOverwritesEnvironmentVariable() {
		assertThat(nokeeVersionSystemPropertyProvider, overrides(nokeeVersionEnvironmentVariableProvider));
	}

	@Test
	void systemPropertyOverwritesCacheFile() {
		assertThat(nokeeVersionSystemPropertyProvider, overrides(nokeeVersionCacheFileProvider));
	}

	@Test
	void systemPropertyOverwritesGradleWrapperProperty() {
		assertThat(nokeeVersionSystemPropertyProvider, overrides(nokeeVersionGradleWrapperPropertyProvider));
	}
	//endregion

	//region Ordering of Gradle properties
	@Test
	void gradlePropertyOverwritesOldGradleProperty() {
		assertThat(nokeeVersionGradlePropertyProvider, overrides(useNokeeVersionGradlePropertyProvider));
	}

	@Test
	void gradlePropertyOverwritesEnvironmentVariable() {
		assertThat(nokeeVersionGradlePropertyProvider, overrides(nokeeVersionEnvironmentVariableProvider));
	}

	@Test
	void gradlePropertyOverwritesCacheFile() {
		assertThat(nokeeVersionGradlePropertyProvider, overrides(nokeeVersionCacheFileProvider));
	}

	@Test
	void gradlePropertyGradleWrapperProperty() {
		assertThat(nokeeVersionGradlePropertyProvider, overrides(nokeeVersionGradleWrapperPropertyProvider));
	}
	//endregion

	//region Ordering of environment variables
	@Test
	void environmentVariableOverwritesOldEnvironmentVariable() {
		assertThat(nokeeVersionEnvironmentVariableProvider, overrides(useNokeeVersionEnvironmentVariableProvider));
	}

	@Test
	void environmentVariableOverwritesCacheFile() {
		assertThat(nokeeVersionEnvironmentVariableProvider, overrides(nokeeVersionCacheFileProvider));
	}

	@Test
	void environmentVariableGradleWrapperProperty() {
		assertThat(nokeeVersionEnvironmentVariableProvider, overrides(nokeeVersionGradleWrapperPropertyProvider));
	}
	//endregion

	//region Ordering of cache file
	@Test
	void cacheFileOverwritesOldCacheFile() {
		assertThat(nokeeVersionCacheFileProvider, overrides(useNokeeVersionCacheFileProvider));
	}

	@Test
	void cacheFileOverwritesGradleWrapperProperty() {
		assertThat(nokeeVersionCacheFileProvider, overrides(nokeeVersionGradleWrapperPropertyProvider));
	}
	//endregion

	//region Ordering of Gradle wrapper properties
	@Test
	void gradleWrapperPropertyOverwritesOldGradleWrapperProperty() {
		assertThat(nokeeVersionGradleWrapperPropertyProvider, overrides(useNokeeVersionGradleWrapperPropertyProvider));
	}
	//endregion

	@SneakyThrows
	private void assertThat(NokeeVersionProvider second, NokeeVersionProvider first) {
		reset(first, second);
		when(first.get()).thenReturn(of(NokeeVersion.parse("0.3.0")));
		when(second.get()).thenReturn(of(NokeeVersion.parse("0.4.0")));
		MatcherAssert.assertThat(subject.call(), equalTo(NokeeVersion.parse("0.4.0")));
	}

	private static NokeeVersionProvider overrides(NokeeVersionProvider provider) {
		return provider;
	}
}
