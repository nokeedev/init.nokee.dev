package dev.nokee.init.internal.versions;

import dev.nokee.init.internal.utils.ProviderUtils;
import org.gradle.api.file.FileSystemLocation;
import org.gradle.api.initialization.Settings;
import org.gradle.api.provider.Provider;
import org.gradle.api.provider.ProviderFactory;

import java.io.File;
import java.util.Optional;
import java.util.Set;

public final class BuildscriptNokeeVersionProvider implements NokeeVersionProvider {
	private static final NokeeVersionFactory FACTORY = new NokeeVersionFactory(NokeeVersionSource.SettingsBuildscriptBlock);
	private final Provider<NokeeVersion> provider;

	public BuildscriptNokeeVersionProvider(ProviderFactory providers, Settings settings) {
		this.provider = Optional.ofNullable(settings.getBuildscript().getConfigurations().findByName("classpath")).map(it -> ProviderUtils.forUseAtConfigurationTime(it.getIncoming().getFiles().getElements()).map(this::inferNokeeVersion)).orElseGet(() -> ProviderUtils.forUseAtConfigurationTime(providers.provider(() -> null)));

//		Optional.ofNullable(settings.getBuildscript().getConfigurations().findByName("classpath")).ifPresent(it -> {
//			System.out.println("WAT " + it.getIncoming().getFiles().getFiles().size());
//			it.getIncoming().getFiles().getFiles().forEach(System.out::println);
//		});

//		System.out.println(it.getBuildscript().getConfigurations().stream().map(Configuration::getName).collect(Collectors.toList()));
//		System.out.println(it.getBuildscript().getConfigurations().getByName("classpath").getIncoming().getFiles().getFiles());
//		System.out.println(settings.getBuildscript().getClassLoader());
//		((CachingClassLoader) settings.getBuildscript().getClassLoader()).visit(new ClassLoaderVisitor() {
//			@Override
//			public void visitClassPath(URL[] classPath) {
//				Arrays.stream(classPath).forEach(System.out::println);
//			}
//		});
	}

	private NokeeVersion inferNokeeVersion(Set<FileSystemLocation> files) {
		return files.stream().map(FileSystemLocation::getAsFile).filter(it -> {
			System.out.println("YO " + it);
			return it.getAbsolutePath().contains("/dev.nokee/");
		}).map(it -> {
//			System.out.println(it);
			return it.getName().replace(".jar", "").split("-")[1];
		}).findFirst().map(FACTORY::create).orElse(null);
	}



	@Override
	public Optional<NokeeVersion> get() {
		return Optional.ofNullable(provider.getOrNull());
	}
}
