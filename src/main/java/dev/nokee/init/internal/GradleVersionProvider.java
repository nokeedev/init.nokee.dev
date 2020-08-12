package dev.nokee.init.internal;

import org.gradle.util.GradleVersion;

public interface GradleVersionProvider {
    GradleVersion get();
}
