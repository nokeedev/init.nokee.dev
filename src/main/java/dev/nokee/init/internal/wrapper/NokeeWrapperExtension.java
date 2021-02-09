package dev.nokee.init.internal.wrapper;

import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.provider.Property;

public abstract class NokeeWrapperExtension {

	public abstract Property<String> getVersion();
	public abstract RegularFileProperty getInitScriptFile();
}
