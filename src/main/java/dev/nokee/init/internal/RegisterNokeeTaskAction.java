package dev.nokee.init.internal;

import dev.nokee.init.internal.versions.BuildClasspathNokeeVersionProvider;
import lombok.val;
import org.gradle.api.Action;
import org.gradle.api.Project;
import org.gradle.api.provider.Provider;
import org.gradle.api.provider.ProviderFactory;
import org.gradle.api.tasks.TaskReference;

import java.util.List;
import java.util.stream.Collectors;

public final class RegisterNokeeTaskAction implements Action<Project> {
	@Override
	public void execute(Project project) {
		val extensionProvider = project.provider(() -> project.getExtensions().getByType(NokeeExtension.class));
		project.getTasks().register("nokee", NokeeTask.class, task -> {
			task.dependsOn(includedBuildsNokeeTasks(project));
			task.setDescription("Configures Gradle integration with Nokee plugins.");
			task.setGroup("Build Setup");

			// TODO: Move the provider into the extension
			task.getCurrentVersion().set(extensionProvider.flatMap(it -> it.getVersion().orElse(task.getProviders().provider(() -> new BuildClasspathNokeeVersionProvider(task.getProject()).get().orElse(null)))));

			task.getShowVersion().convention(showVersionAlternateFlag(task.getProviders()));
			task.getBuildIdentityPath().convention(extensionProvider.flatMap(NokeeExtension::getIdentityPath)).disallowChanges();
		});
	}

	private List<TaskReference> includedBuildsNokeeTasks(Project project) {
		return project.getGradle().getIncludedBuilds().stream().map(it -> it.task(":nokee")).collect(Collectors.toList());
	}

	private static Provider<Boolean> showVersionAlternateFlag(ProviderFactory providers) {
		return providers.provider(() -> System.getProperties().containsKey("show-version"));
	}
}
