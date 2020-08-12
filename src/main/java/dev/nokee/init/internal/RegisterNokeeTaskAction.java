package dev.nokee.init.internal;

import org.gradle.api.Action;
import org.gradle.api.Project;

public final class RegisterNokeeTaskAction implements Action<Project> {
    @Override
    public void execute(Project project) {
        project.getTasks().register("nokee", NokeeTask.class, new ConfigureNokeeTaskAction());
    }
}
