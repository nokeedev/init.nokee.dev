package dev.nokee.init.internal.buildinit;

import org.gradle.api.Action;
import org.gradle.api.Project;

public final class OnlyIfInitTaskIsRequestedAction implements Action<Project> {
    private final Action<Project> delegate;

    public OnlyIfInitTaskIsRequestedAction(Action<Project> delegate) {
        this.delegate = delegate;
    }

    @Override
    public void execute(Project project) {
        if (project.getGradle().getStartParameter().getTaskNames().stream().anyMatch(it -> it.equals("init") || it.equals(":init"))) {
            delegate.execute(project);
        }
    }
}
