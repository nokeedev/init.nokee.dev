package dev.nokee.init.internal.accessors;

import javax.annotation.Nullable;

public interface GradlePropertyAccessor {
	@Nullable
	String get(String name);
}
