package dev.nokee.init.internal.wrapper;

import org.gradle.api.Action;
import org.gradle.api.Project;
import org.gradle.api.tasks.wrapper.Wrapper;

public final class RegisterWrapperTaskConfigurationAction implements Action<Project> {
	@Override
	public void execute(Project project) {
		project.getTasks().named("wrapper", Wrapper.class, new RegisterNokeeWrapperExtensionAction(project.getObjects(), project.getLayout()));
	}
}
