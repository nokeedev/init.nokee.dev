package dev.nokee.init.internal;

import org.gradle.api.Action;
import org.gradle.api.Project;
import org.gradle.api.internal.GradleInternal;

public final class RegisterNokeeTaskAction implements Action<Project> {
	@Override
	public void execute(Project project) {
		project.getTasks().register("nokee", NokeeTask.class, new ConfigureNokeeTaskAction(((GradleInternal) project.getGradle()).getSettings().getExtensions().getByType(NokeeExtension.class)));
	}
}
