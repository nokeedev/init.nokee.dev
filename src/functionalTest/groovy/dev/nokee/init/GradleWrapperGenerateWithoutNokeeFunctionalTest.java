package dev.nokee.init;

import dev.gradleplugins.runnerkit.GradleExecutor;
import dev.gradleplugins.runnerkit.GradleRunner;
import lombok.val;
import org.apache.commons.io.FileUtils;
import org.hamcrest.Matchers;
import org.hamcrest.io.FileMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import static dev.nokee.init.fixtures.GradleRunnerUtils.configurePluginClasspathAsInitScriptDependencies;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.commons.io.FileUtils.readLines;
import static org.gradle.util.GUtil.loadProperties;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GradleWrapperGenerateWithoutNokeeFunctionalTest {
	@TempDir
	Path testDirectory;

	@BeforeEach
	void generateWrapperWithNokeeIntegration() throws IOException {
		val initScriptFile = testDirectory.resolve("init.gradle").toFile();
		FileUtils.write(initScriptFile, configurePluginClasspathAsInitScriptDependencies(), UTF_8);
		FileUtils.write(initScriptFile, "\napply plugin: dev.nokee.init.NokeeInitPlugin\n", UTF_8, true);
		GradleRunner.create(GradleExecutor.gradleTestKit())
			.inDirectory(testDirectory.toFile())
			.withArguments("wrapper")
			.usingInitScript(initScriptFile)
			.build();
	}

	@Test
	void doesNotWriteNokeeInitScript() {
		assertThat("gradle/nokee.init.gradle should not exists",
			testDirectory.resolve("gradle/nokee.init.gradle").toFile(),
			not(FileMatchers.anExistingFile()));
	}

	@Test
	void doesNotPatchGradleWrapperScript() throws IOException {
		assertTrue(readLines(testDirectory.resolve("gradlew").toFile(), UTF_8).stream().noneMatch(this::scriptPath));
		assertTrue(readLines(testDirectory.resolve("gradlew.bat").toFile(), UTF_8).stream().noneMatch(this::scriptPath));
	}

	private boolean scriptPath(String line) {
		return line.contains("nokee.init.gradle");
	}

	@Test
	void doesNotIncludeNokeeVersionGradleWrapperProperty() {
		assertThat("gradle-wrapper.properties should not contains nokeeVersion property",
			loadProperties(testDirectory.resolve("gradle/wrapper/gradle-wrapper.properties").toFile()),
			not(hasKey("nokeeVersion")));
	}
}
