package dev.nokee.init.internal.accessors;

import javax.annotation.Nullable;

public interface EnvironmentVariableAccessor {
    @Nullable
    String get(String name);
}
