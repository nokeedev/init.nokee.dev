package dev.nokee.init.internal;

import org.gradle.api.Action;
import org.gradle.api.Plugin;
import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;

public final class DuplicateNokeeInitPluginLoadsDetectionAction implements Action<Plugin> {
	private final Callback callback;
	private NokeeInitPluginContext previousPlugin = null;

	DuplicateNokeeInitPluginLoadsDetectionAction(Callback callback) {
		this.callback = callback;
	}

	public static Action<Plugin> disableDuplicateNokeeInitPluginLoads() {
		return new DuplicateNokeeInitPluginLoadsDetectionAction(new ResolveConflict());
	}

	@Override
	public void execute(Plugin currentPlugin) {
		if (isNokeeInitPlugin(currentPlugin)) {
			if (previousPlugin != null) {
				previousPlugin = callback.onDuplicateLoads(previousPlugin, new NokeeInitPluginContext(currentPlugin));
			} else {
				previousPlugin = new NokeeInitPluginContext(currentPlugin);
			}
		}
	}

	private static boolean isNokeeInitPlugin(Plugin plugin) {
		return plugin.getClass().getSimpleName().equals("NokeeInitPlugin");
	}

	interface Callback {
		NokeeInitPluginContext onDuplicateLoads(NokeeInitPluginContext first, NokeeInitPluginContext second);
	}

	private static final class ResolveConflict implements Callback {
		private static final Logger LOGGER = Logging.getLogger(ResolveConflict.class);
		@Override
		public NokeeInitPluginContext onDuplicateLoads(NokeeInitPluginContext first, NokeeInitPluginContext second) {
			LOGGER.warn("WARNING: Multiple Nokee init plugin loaded, keeping one of them.");
			LOGGER.warn(" \\-> Learn more at https://github.com/nokeedev/init.nokee.dev#runtime-conflict");
			if (first.canDisable()) {
				if (second.canDisable()) {
					if (first.getVersion().compareTo(second.getVersion()) < 0) {
						LOGGER.debug("Disabling " + first + " because " + second + " is older.");
						first.disable();
						return second;
					} else {
						LOGGER.debug("Disabling " + second + " because " + first + " is older.");
						second.disable();
						return first;
					}
				} else {
					LOGGER.debug("Disabling " + first + " because " + second + " cannot be disabled.");
					first.disable();
					return second;
				}
			} else {
				if (second.canDisable()) {
					LOGGER.debug("Disabling " + second + " because " + first + " cannot be disabled.");
					second.disable();
					return first;
				} else {
					LOGGER.debug("Keeping both as none can be disabled.");
					return second;
				}
			}
		}
	}
}
