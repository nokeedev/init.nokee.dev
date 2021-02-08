package dev.nokee.init.internal.versions;

import dev.nokee.init.internal.utils.GradleUtils;
import org.gradle.api.file.RegularFile;
import org.gradle.api.initialization.Settings;
import org.gradle.api.provider.ProviderFactory;

import java.io.File;

public class DefaultNokeeVersionProviderFactory implements NokeeVersionProviderFactory {
	private final ProviderFactory providers;
	private final Settings settings;

	public DefaultNokeeVersionProviderFactory(ProviderFactory providers, Settings settings) {
		this.providers = providers;
		this.settings = settings;
	}

	@Override
	public NokeeVersionProvider systemProperty(String propertyName) {
		return new SystemPropertyNokeeVersionProvider(propertyName, providers);
	}

	@Override
	public NokeeVersionProvider gradleProperty(String propertyName) {
		return new GradlePropertyNokeeVersionProvider(propertyName, providers);
	}

	@Override
	public NokeeVersionProvider environmentVariable(String variableName) {
		return new EnvironmentVariableNokeeVersionProvider(variableName, providers);
	}

	@Override
	public NokeeVersionProvider cacheFile(String path) {
		return new CacheFileNokeeVersionProvider(new File(GradleUtils.getRootBuildDirectory(settings), path));
	}

	@Override
	public NokeeVersionProvider buildscript() {
		return new BuildscriptNokeeVersionProvider(providers, settings);
	}

	@Override
	public NokeeVersionProvider gradleWrapperProperty(String propertyName) {
		return new GradleWrapperPropertiesNokeeVersionProvider(propertyName, providers, settings);
	}
}
