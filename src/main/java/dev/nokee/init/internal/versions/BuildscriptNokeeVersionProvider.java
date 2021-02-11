package dev.nokee.init.internal.versions;

import dev.nokee.init.internal.utils.ProviderUtils;
import org.gradle.api.file.FileSystemLocation;
import org.gradle.api.initialization.Settings;
import org.gradle.api.provider.Provider;
import org.gradle.api.provider.ProviderFactory;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static dev.nokee.init.internal.versions.NokeeVersionClassPathUtils.inferVersion;

public final class BuildscriptNokeeVersionProvider implements NokeeVersionProvider {
	private static final NokeeVersionFactory FACTORY = new NokeeVersionFactory(NokeeVersionSource.SettingsBuildscriptBlock);
	private final Provider<NokeeVersion> provider;

	public BuildscriptNokeeVersionProvider(ProviderFactory providers, Settings settings) {
		this.provider = Optional.ofNullable(settings.getBuildscript().getConfigurations().findByName("classpath")).map(it -> ProviderUtils.forUseAtConfigurationTime(it.getIncoming().getFiles().getElements()).map(this::inferNokeeVersion)).orElseGet(() -> ProviderUtils.forUseAtConfigurationTime(providers.provider(() -> null)));
	}

	private NokeeVersion inferNokeeVersion(Set<FileSystemLocation> files) {
		return inferVersion(files.stream().map(FileSystemLocation::getAsFile).collect(Collectors.toSet()))
			.map(FACTORY::create)
			.orElse(null);
	}

	@Override
	public Optional<NokeeVersion> get() {
		return Optional.ofNullable(provider.getOrNull());
	}
}
