package dev.nokee.init.internal.accessors;

import javax.annotation.Nullable;

public interface SystemPropertyAccessor {
	@Nullable
	String get(String name);
}
