package dev.nokee.init.internal.wrapper;

import lombok.val;
import org.gradle.api.Action;
import org.gradle.api.Task;
import org.gradle.api.provider.Provider;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

import static dev.nokee.init.internal.utils.IOUtils.closeQuietly;
import static dev.nokee.init.internal.utils.IOUtils.copy;

public final class GradleWrapperActionNokeeInitScriptWriter implements Action<Task> {
	private final Provider<File> nokeeInitScriptFileProvider;

	public GradleWrapperActionNokeeInitScriptWriter(Provider<File> nokeeInitScriptFileProvider) {
		this.nokeeInitScriptFileProvider = nokeeInitScriptFileProvider;
	}

	@Override
	public void execute(Task task) {
		InputStream inStream = this.getClass().getResourceAsStream("nokee.init.gradle");
		Objects.requireNonNull(inStream, "Could not find the Nokee init script inside the classpath.");

		try (val outStream = new FileOutputStream(nokeeInitScriptFileProvider.get())) {
			copy(inStream, outStream);
		} catch (IOException e) {
			throw new UncheckedIOException(String.format("Could not write Nokee init script file at '%s'.", nokeeInitScriptFileProvider.get().getAbsolutePath()), e);
		} finally {
			closeQuietly(inStream);
		}
	}
}
