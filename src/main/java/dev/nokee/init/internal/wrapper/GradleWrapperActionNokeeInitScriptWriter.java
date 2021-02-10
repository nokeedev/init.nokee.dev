package dev.nokee.init.internal.wrapper;

import org.gradle.api.Action;
import org.gradle.api.Task;
import org.gradle.api.provider.Provider;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

import static dev.nokee.init.internal.utils.IOUtils.closeQuietly;

public final class GradleWrapperActionNokeeInitScriptWriter implements Action<Task> {
	private final Provider<File> nokeeInitScriptFileProvider;

	public GradleWrapperActionNokeeInitScriptWriter(Provider<File> nokeeInitScriptFileProvider) {
		this.nokeeInitScriptFileProvider = nokeeInitScriptFileProvider;
	}

	@Override
	public void execute(Task task) {
		InputStream inStream = this.getClass().getResourceAsStream("nokee.init.gradle");
		Objects.requireNonNull(inStream, "Could not find the Nokee init script inside the classpath.");

		try {
			Files.copy(inStream, nokeeInitScriptFileProvider.get().toPath(), StandardCopyOption.REPLACE_EXISTING);
			nokeeInitScriptFileProvider.get().setExecutable(false, false);
		} catch (IOException e) {
			throw new UncheckedIOException(String.format("Could not write Nokee init script file at '%s'.", nokeeInitScriptFileProvider.get().getAbsolutePath()), e);
		} finally {
			closeQuietly(inStream);
		}
	}
}
