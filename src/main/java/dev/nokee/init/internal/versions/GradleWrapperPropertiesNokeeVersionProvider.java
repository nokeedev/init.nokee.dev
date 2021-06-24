package dev.nokee.init.internal.versions;

import dev.nokee.init.internal.utils.GradleUtils;
import lombok.val;
import org.gradle.api.initialization.Settings;
import org.gradle.api.provider.Provider;
import org.gradle.api.provider.ProviderFactory;
import org.gradle.util.GUtil;

import javax.annotation.Nullable;
import java.io.File;
import java.util.Optional;
import java.util.Properties;

import static dev.nokee.init.internal.utils.ProviderUtils.forUseAtConfigurationTime;

public final class GradleWrapperPropertiesNokeeVersionProvider implements NokeeVersionProvider {
	private static final NokeeVersionFactory FACTORY = new NokeeVersionFactory(NokeeVersionSource.GradleWrapperProperty);
	@Nullable private final Provider<NokeeVersion> provider;
	private final String propertyName;

	public GradleWrapperPropertiesNokeeVersionProvider(String propertyName, ProviderFactory providers, Settings settings) {
		this.propertyName = propertyName;
		this.provider = forUseAtConfigurationTime(providers.provider(() -> findGradleWrapperProperties(GradleUtils.getRootBuildDirectory(settings)).map(GUtil::loadProperties).flatMap(this::nokeeVersion).map(FACTORY::create).orElse(null)));
	}

	private static Optional<File> findGradleWrapperProperties(File rootBuildDirectory) {
		val gradleWrapperPropertiesFile = new File(rootBuildDirectory, "gradle/wrapper/gradle-wrapper.properties");
		if (gradleWrapperPropertiesFile.exists()) {
			return Optional.of(gradleWrapperPropertiesFile);
		}
		return Optional.empty();
	}

	private Optional<String> nokeeVersion(Properties gradleWrapperProperties) {
		return Optional.ofNullable(gradleWrapperProperties.getProperty(propertyName, null));
	}

	@Override
	public Optional<NokeeVersion> get() {
		return Optional.ofNullable(provider.getOrNull());
	}
}
