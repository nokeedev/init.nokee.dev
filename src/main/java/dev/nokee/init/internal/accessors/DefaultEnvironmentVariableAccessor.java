package dev.nokee.init.internal.accessors;

import javax.annotation.Nullable;

public final class DefaultEnvironmentVariableAccessor implements EnvironmentVariableAccessor {
    public static final DefaultEnvironmentVariableAccessor INSTANCE = new DefaultEnvironmentVariableAccessor();

    @Nullable
    @Override
    public String get(String name) {
        return System.getenv(name);
    }
}
