package dev.nokee.init.internal.commands;

import dev.nokee.init.internal.versions.NokeeVersion;
import dev.nokee.init.internal.versions.NokeeVersionWriter;
import lombok.val;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Optional;
import java.util.function.Supplier;

public final class ConfigureNokeeVersionCommand implements Runnable {
	public static final String FLAG = "use-version";
	public static final String HELP_MESSAGE = "Specifies the nokee version to use in this project.";
	private final Supplier<String> nokeeVersion;
	private final File projectDirectory;

	public ConfigureNokeeVersionCommand(Supplier<String> nokeeVersion, File projectDirectory) {
		this.nokeeVersion = nokeeVersion;
		this.projectDirectory = projectDirectory;
	}

	@Override
	public void run() {
		Optional.ofNullable(nokeeVersion.get()).ifPresent(version -> {
			try (val writer = new NokeeVersionWriter(new FileOutputStream(new File(projectDirectory, ".gradle/nokee-version.txt")))) {
				writer.write(NokeeVersion.parse(version));
			} catch (IOException e) {
				throw new UncheckedIOException(e);
			}
		});
	}
}
