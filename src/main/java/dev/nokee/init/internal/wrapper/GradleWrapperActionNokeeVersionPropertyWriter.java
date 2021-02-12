package dev.nokee.init.internal.wrapper;

import lombok.val;
import org.gradle.api.Action;
import org.gradle.api.Task;
import org.gradle.api.provider.Provider;

import java.io.*;
import java.util.Properties;
import java.util.TreeMap;

public final class GradleWrapperActionNokeeVersionPropertyWriter implements Action<Task> {
	private final Provider<String> nokeeVersionProvider;
	private final Provider<File> propertiesFileProvider;

	public GradleWrapperActionNokeeVersionPropertyWriter(Provider<String> nokeeVersionProvider, Provider<File> propertiesFileProvider) {
		this.nokeeVersionProvider = nokeeVersionProvider;
		this.propertiesFileProvider = propertiesFileProvider;
	}

	@Override
	public void execute(Task task) {
		Properties properties = new Properties();
		readWrapperProperties(properties);
		properties.put("nokeeVersion", nokeeVersionProvider.get());
		writeWrapperProperties(properties);
	}

	private void readWrapperProperties(Properties properties) {
		try (val inStream = new FileInputStream(propertiesFileProvider.get())) {
			properties.load(inStream);
		} catch (IOException e) {
			throw new UncheckedIOException(String.format("Could not read the wrapper properties file at '%s'.", propertiesFileProvider.get().getAbsolutePath()), e);
		}
	}

	private void writeWrapperProperties(Properties properties) {
		try (val out = new PrintWriter(new FileOutputStream(propertiesFileProvider.get()))) {
			new TreeMap<>(properties).forEach((key, value) -> {
				out.println(key + "=" + value);
			});
		} catch (IOException e) {
			throw new UncheckedIOException(String.format("Could not write the wrapper properties file at '%s'.", propertiesFileProvider.get().getAbsolutePath()), e);
		}
	}
}
