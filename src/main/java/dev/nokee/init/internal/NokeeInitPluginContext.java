package dev.nokee.init.internal;

import lombok.EqualsAndHashCode;
import lombok.val;
import org.gradle.util.VersionNumber;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

import static org.gradle.util.VersionNumber.parse;

@EqualsAndHashCode
public final class NokeeInitPluginContext {
	private final Object plugin;

	// We use Object here because both plugin will be coming from different classloader so the type is irrelevant
	public NokeeInitPluginContext(Object plugin) {
		this.plugin = plugin;
	}

	public boolean canDisable() {
		try {
			plugin.getClass().getMethod("disable");
			return true;
		} catch (NoSuchMethodException e) {
			return false;
		}
	}

	public void disable() {
		try {
			val method = plugin.getClass().getMethod("disable");
			method.invoke(plugin);
		} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	public VersionNumber getVersion() {
		try {
			val method = plugin.getClass().getMethod("getVersion");
			return Optional.ofNullable((String) method.invoke(plugin)).map(VersionNumber::parse).orElse(parse("0.0.0"));
		} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
			return parse("0.0.0");
		}
	}


	@Override
	public String toString() {
		return "plugin '" + plugin.getClass().getCanonicalName() + "' version '" + getVersion() + "'";
	}
}
