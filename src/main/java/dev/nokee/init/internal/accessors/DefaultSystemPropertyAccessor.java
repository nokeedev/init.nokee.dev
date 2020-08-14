package dev.nokee.init.internal.accessors;

import javax.annotation.Nullable;

public final class DefaultSystemPropertyAccessor implements SystemPropertyAccessor {
    @Nullable
    @Override
    public String get(String name) {
        return System.getProperty(name);
    }
}
