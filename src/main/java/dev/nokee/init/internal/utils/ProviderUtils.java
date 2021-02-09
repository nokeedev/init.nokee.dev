package dev.nokee.init.internal.utils;

import org.gradle.api.provider.Provider;
import org.gradle.api.provider.ProviderFactory;
import org.gradle.internal.Cast;
import org.gradle.util.GradleVersion;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class ProviderUtils {
	private ProviderUtils() {}

	// TODO(grava): Move this in grava-utils
	public static <S> Provider<S> forUseAtConfigurationTime(Provider<S> provider) {
		if (GradleVersion.current().compareTo(GradleVersion.version("6.5")) >= 0) {
			try {
				Method method = Provider.class.getMethod("forUseAtConfigurationTime");
				return Cast.uncheckedCast(method.invoke(provider));
			} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
				throw new RuntimeException("Could not mark provider usage for configuration time because of an exception.", e);
			}
		}
		return provider;
	}
}
