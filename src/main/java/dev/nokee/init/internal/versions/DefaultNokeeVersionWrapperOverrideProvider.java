package dev.nokee.init.internal.versions;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class DefaultNokeeVersionWrapperOverrideProvider implements Callable<NokeeVersion> {
	private final NokeeVersionProviderFactory factory;

	public DefaultNokeeVersionWrapperOverrideProvider(NokeeVersionProviderFactory factory) {
		this.factory = factory;
	}

	@Nullable
	@Override
	public NokeeVersion call() throws Exception {
		return factory.systemProperty("nokee-version").get().orElseGet(() -> factory.gradleProperty("nokee-version").get().orElse(null));
	}
}
