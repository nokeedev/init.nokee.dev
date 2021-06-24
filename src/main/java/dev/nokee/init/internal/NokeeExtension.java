package dev.nokee.init.internal;

import dev.nokee.init.internal.utils.GradleUtils;
import dev.nokee.init.internal.versions.DefaultNokeeVersionProvider;
import dev.nokee.init.internal.versions.DefaultNokeeVersionProviderFactory;
import dev.nokee.init.internal.versions.NokeeVersion;
import org.gradle.api.initialization.Settings;
import org.gradle.api.provider.Property;
import org.gradle.api.provider.ProviderFactory;
import org.gradle.util.Path;

import javax.inject.Inject;

public abstract class NokeeExtension {
	@Inject
	public NokeeExtension(ProviderFactory providers, Settings settings) {
		getIdentityPath().value(GradleUtils.getIdentityPath(settings.getGradle())).disallowChanges();
		getVersion().convention(providers.provider(new DefaultNokeeVersionProvider(new DefaultNokeeVersionProviderFactory(providers, settings)))).finalizeValueOnRead();
	}

	public abstract Property<NokeeVersion> getVersion();

	public abstract Property<Path> getIdentityPath();
}
