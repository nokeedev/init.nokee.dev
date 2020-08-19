package dev.nokee.init.internal.accessors;

import org.gradle.api.Project;

import javax.annotation.Nullable;
import java.util.Objects;

public final class DefaultGradlePropertyAccessor implements GradlePropertyAccessor {
    private final Project project;

    public DefaultGradlePropertyAccessor(Project project) {
        this.project = project;
    }

    @Nullable
    @Override
    public String get(String name) {
        return Objects.toString(project.findProperty(name), null);
    }
}
