package dev.nokee.init.internal;

import org.gradle.api.Action;

public final class ConfigureNokeeTaskAction implements Action<NokeeTask> {
    @Override
    public void execute(NokeeTask task) {
        task.setDescription("Configures Gradle integration with Nokee plugins.");
        task.setGroup("Build Setup");
    }
}
