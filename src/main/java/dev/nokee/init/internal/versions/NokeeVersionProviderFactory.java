package dev.nokee.init.internal.versions;

import org.gradle.api.file.RegularFile;

public interface NokeeVersionProviderFactory {
	NokeeVersionProvider systemProperty(String propertyName);
	NokeeVersionProvider gradleProperty(String propertyName);
	NokeeVersionProvider environmentVariable(String variableName);
	NokeeVersionProvider cacheFile(String path);
	NokeeVersionProvider buildscript();
	NokeeVersionProvider gradleWrapperProperty(String propertyName);
}
