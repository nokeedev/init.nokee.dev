package dev.nokee.init.internal.wrapper;

import lombok.val;
import org.gradle.api.Action;
import org.gradle.api.Task;
import org.gradle.api.provider.Provider;

import java.io.*;

public final class GradleWrapperActionScriptPatcher implements Action<Task> {
	private final Provider<File> scriptFileProvider;
	private final Provider<File> initScriptFileProvider;

	public GradleWrapperActionScriptPatcher(Provider<File> scriptFileProvider, Provider<File> initScriptFileProvider) {
		this.scriptFileProvider = scriptFileProvider;
		this.initScriptFileProvider = initScriptFileProvider;
	}

	@Override
	public void execute(Task task) {
		String pathToInitScript = scriptFileProvider.get().getParentFile().toPath().relativize(initScriptFileProvider.get().toPath()).toString();

		File bashScriptFile = scriptFileProvider.get();
		try (val reader = new GradleWrapperScriptReader(new FileInputStream(bashScriptFile))) {
			val script = reader.read().patch(pathToInitScript);
			try (val writer = new GradleWrapperScriptWriter(new FileOutputStream(bashScriptFile))) {
				writer.write(script);
			}
		} catch (IOException e) {
			throw new UncheckedIOException(String.format("Could not patch wrapper script at '%s'.", bashScriptFile.getAbsolutePath()), e);
		}

		File batchScriptFile = new File(scriptFileProvider.get().getAbsolutePath() + ".bat");
		try (val reader = new GradleWrapperScriptReader(new FileInputStream(batchScriptFile))) {
			val script = reader.read().patch(pathToInitScript);
			try (val writer = new GradleWrapperScriptWriter(new FileOutputStream(batchScriptFile))) {
				writer.write(script);
			}
		} catch (IOException e) {
			throw new UncheckedIOException(String.format("Could not patch wrapper script at '%s'.", batchScriptFile.getAbsolutePath()), e);
		}
	}
}
