package dev.nokee.init.internal.wrapper;

import lombok.val;
import org.gradle.api.Action;
import org.gradle.api.Task;
import org.gradle.api.provider.Provider;
import org.gradle.internal.util.PropertiesUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Properties;

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
		try {
			PropertiesUtils.store(properties, propertiesFileProvider.get());
		} catch (IOException e) {
			throw new UncheckedIOException(String.format("Could not write the wrapper properties file at '%s'.", propertiesFileProvider.get().getAbsolutePath()), e);
		}
	}
}
