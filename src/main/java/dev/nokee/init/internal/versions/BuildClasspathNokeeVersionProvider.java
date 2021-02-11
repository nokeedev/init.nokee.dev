package dev.nokee.init.internal.versions;

import lombok.val;
import org.gradle.api.Project;
import org.gradle.internal.classloader.ClassLoaderHierarchy;
import org.gradle.internal.classloader.ClassLoaderVisitor;

import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;

public final class BuildClasspathNokeeVersionProvider implements NokeeVersionProvider {
	private static final NokeeVersionFactory FACTORY = new NokeeVersionFactory(NokeeVersionSource.BuildClasspath);
	private final Project project;

	public BuildClasspathNokeeVersionProvider(Project project) {
		this.project = project;
	}

	@Override
	public Optional<NokeeVersion> get() {
		if (project.getBuildscript().getClassLoader() instanceof ClassLoaderHierarchy) {
			val paths = new ArrayList<NokeeVersion>();
			((ClassLoaderHierarchy) project.getBuildscript().getClassLoader()).visit(new ClassLoaderVisitor() {
				@Override
				public void visitClassPath(URL[] classPath) {
					NokeeVersionClassPathUtils.inferVersion(classPath).map(FACTORY::create).ifPresent(paths::add);
				}
			});
			return paths.stream().findFirst();
		}
		return Optional.empty();
	}
}
