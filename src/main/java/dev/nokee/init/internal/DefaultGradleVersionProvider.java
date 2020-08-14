package dev.nokee.init.internal;

import org.gradle.util.GradleVersion;

public final class DefaultGradleVersionProvider implements GradleVersionProvider {
    @Override
    public GradleVersion get() {
        return GradleVersion.current();
    }
}
