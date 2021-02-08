package dev.nokee.init.internal.versions;

import lombok.val;
import org.gradle.api.Project;
import org.gradle.api.file.FileSystemLocation;
import org.gradle.api.initialization.Settings;
import org.gradle.internal.classloader.CachingClassLoader;
import org.gradle.internal.classloader.ClassLoaderHierarchy;
import org.gradle.internal.classloader.ClassLoaderVisitor;
import org.gradle.internal.classloader.VisitableURLClassLoader;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

public final class BuildClasspathNokeeVersionProvider implements NokeeVersionProvider {
	private static final NokeeVersionFactory FACTORY = new NokeeVersionFactory(NokeeVersionSource.BuildClasspath);
	private final Project project;

	public BuildClasspathNokeeVersionProvider(Project project) {
		this.project = project;
//		System.out.println(project.getBuildscript().getClassLoader());
//		if (settings.getBuildscript().getClassLoader() instanceof VisitableURLClassLoader) {
//		((VisitableURLClassLoader) settings.getBuildscript().getClassLoader()).visit(new ClassLoaderVisitor() {
//			@Override
//			public void visitClassPath(URL[] classPath) {
//				Arrays.stream(classPath).forEach(System.out::println);
//			}
//		});
//
//		}
//		if (project.getBuildscript().getClassLoader() instanceof ClassLoaderHierarchy) {
//			((ClassLoaderHierarchy) project.getBuildscript().getClassLoader()).visit(new ClassLoaderVisitor() {
//				@Override
//				public void visitClassPath(URL[] classPath) {
//					Arrays.stream(classPath).forEach(System.out::println);
//				}
//			});
//		}
	}

	private NokeeVersion inferNokeeVersion(Set<FileSystemLocation> files) {
		return files.stream().map(FileSystemLocation::getAsFile).filter(it -> {
//			System.out.println("YO " + it);
			return it.getAbsolutePath().contains("/dev.nokee/");
		}).map(it -> {
//			System.out.println(it);
			return it.getName().replace(".jar", "").split("-")[1];
		}).findFirst().map(FACTORY::create).orElse(null);
	}


	// TODO: May be needed for included build
//	public BuildClasspathNokeeVersionProvider(Settings settings) {
//		System.out.println(settings.getBuildscript().getClassLoader());
////		if (settings.getBuildscript().getClassLoader() instanceof VisitableURLClassLoader) {
////		((VisitableURLClassLoader) settings.getBuildscript().getClassLoader()).visit(new ClassLoaderVisitor() {
////			@Override
////			public void visitClassPath(URL[] classPath) {
////				Arrays.stream(classPath).forEach(System.out::println);
////			}
////		});
////
////		}
//		if (settings.getBuildscript().getClassLoader() instanceof ClassLoaderHierarchy) {
//			((ClassLoaderHierarchy) settings.getBuildscript().getClassLoader()).visit(new ClassLoaderVisitor() {
//				@Override
//				public void visitClassPath(URL[] classPath) {
//					Arrays.stream(classPath).forEach(System.out::println);
//				}
//			});
//		}
//	}

	@Override
	public Optional<NokeeVersion> get() {
		if (project.getBuildscript().getClassLoader() instanceof ClassLoaderHierarchy) {
			val paths = new ArrayList<File>();
			((ClassLoaderHierarchy) project.getBuildscript().getClassLoader()).visit(new ClassLoaderVisitor() {
				@Override
				public void visitClassPath(URL[] classPath) {
					Arrays.stream(classPath).filter(it -> {
//						System.out.println("YOOOO " + it);
						// TODO: the cache seems to have changed, we should prepend all nokee artifact with nokee-
						// TODO: For now, let's have a list of known artifact baseName and find those
						//   Also add a flag to disable mismatch check (just in case), when the flag is enabled, we should warn on the command line the flag may hide version mismatch causing various errors at runtime, instead, report the error to Nokee project
						//
						return it.getPath().contains("/dev.nokee/") || it.getPath().contains("platformJni");
					}).map(it -> {
						try {
							return it.toURI();
						} catch (URISyntaxException e) {
							throw new RuntimeException(e);
						}
					}).map(File::new).forEach(paths::add);
				}
			});
			return paths.stream().map(it -> it.getName().replace(".jar", "").split("-")[1]).findFirst().map(FACTORY::create);
		}
		return Optional.empty();
	}
}
