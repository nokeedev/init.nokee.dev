package dev.nokee.init.internal.commands;

import dev.nokee.init.internal.ConsolePrinter;
import dev.nokee.init.internal.versions.NokeeVersion;
import org.gradle.api.provider.Provider;
import org.gradle.util.Path;

import java.util.Optional;

public final class ShowVersionCommand implements Runnable {
	public static final String FLAG = "show-version";
	public static final String HELP_MESSAGE = "Print version info.";
	private final ConsolePrinter out;
	private final Provider<NokeeVersion> nokeeVersionProvider;
	private final Provider<Path> buildIdentityPath;

	public ShowVersionCommand(ConsolePrinter out, Provider<NokeeVersion> nokeeVersionProvider, Provider<Path> buildIdentityPath) {
		this.out = out;
		this.nokeeVersionProvider = nokeeVersionProvider;
		this.buildIdentityPath = buildIdentityPath;
	}

	@Override
	public void run() {
		Optional<NokeeVersion> nokeeVersion = Optional.ofNullable(nokeeVersionProvider.getOrNull());
		if (nokeeVersion.isPresent()) {
			out.println("Build '" + buildIdentityPath.get() + "' using Nokee " + nokeeVersion.get() + ".");
		} else {
			out.println("Nokee isn't configured for this project, please use ./gradlew nokee --use-version=<version>");
		}
		out.flush();
	}
}
