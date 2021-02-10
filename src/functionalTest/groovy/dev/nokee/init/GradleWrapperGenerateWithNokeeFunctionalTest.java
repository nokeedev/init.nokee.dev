package dev.nokee.init;

import dev.gradleplugins.runnerkit.GradleExecutor;
import dev.gradleplugins.runnerkit.GradleRunner;
import lombok.val;
import org.apache.commons.io.FileUtils;
import org.gradle.util.GUtil;
import org.hamcrest.Matchers;
import org.hamcrest.io.FileMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFilePermission;

import static dev.nokee.init.fixtures.GradleRunnerUtils.configurePluginClasspathAsInitScriptDependencies;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.attribute.PosixFilePermission.*;
import static org.apache.commons.io.FileUtils.readLines;
import static org.gradle.util.GUtil.loadProperties;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GradleWrapperGenerateWithNokeeFunctionalTest {
	@TempDir
	Path testDirectory;

	@BeforeEach
	void generateWrapperWithNokeeIntegration() throws IOException {
		val initScriptFile = testDirectory.resolve("init.gradle").toFile();
		FileUtils.write(initScriptFile, configurePluginClasspathAsInitScriptDependencies(), UTF_8);
		FileUtils.write(initScriptFile, "\napply plugin: dev.nokee.init.NokeeInitPlugin\n", UTF_8, true);
		GradleRunner.create(GradleExecutor.gradleTestKit())
			.inDirectory(testDirectory.toFile())
			.withArguments("wrapper", "-Dnokee-version=0.4.0")
			.usingInitScript(initScriptFile)
			.build();
	}

	// TODO: check hash instead of a gold init script
	private static File getGoldNokeeInitScriptFile() {
		return new File(System.getProperty("dev.nokee.init.gold-nokee-init-script"));
	}

	@Test
	void writesNokeeInitScript() throws IOException {
		assertThat("gradle/nokee.init.gradle should be the same as the gold init script",
			readLines(testDirectory.resolve("gradle/nokee.init.gradle").toFile(), UTF_8),
			equalTo(readLines(getGoldNokeeInitScriptFile(), UTF_8)));
	}

	@Test
	void patchesGradleWrapperScript() throws IOException {
		assertTrue(readLines(testDirectory.resolve("gradlew").toFile(), UTF_8).stream().anyMatch(this::scriptPath));
		assertTrue(readLines(testDirectory.resolve("gradlew.bat").toFile(), UTF_8).stream().anyMatch(this::scriptPath));
	}

	private boolean scriptPath(String line) {
		return line.contains("nokee.init.gradle");
	}

	@Test
	void includesNokeeVersionGradleWrapperProperty() {
		assertThat("gradle-wrapper.properties should contains nokeeVersion property",
			loadProperties(testDirectory.resolve("gradle/wrapper/gradle-wrapper.properties").toFile()),
			hasEntry("nokeeVersion", "0.4.0"));
	}

	@Test
	void gradleBashScriptIsStillExecutable() {
		assertThat(testDirectory.resolve("gradlew").toFile().canExecute(), equalTo(true));
	}

	@Test
	void initScriptIsNotExecutable() throws IOException {
		assertThat(testDirectory.resolve("gradle/nokee.init.gradle").toFile().canExecute(), equalTo(false));
	}
}
