package dev.nokee.init;

import dev.gradleplugins.runnerkit.GradleExecutor;
import dev.gradleplugins.runnerkit.GradleRunner;
import dev.nokee.init.fixtures.GradleRunnerUtils;
import lombok.val;
import org.apache.commons.io.FileUtils;
import org.hamcrest.io.FileMatchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

import static dev.nokee.init.fixtures.GradleRunnerUtils.configurePluginClasspathAsInitScriptDependencies;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.commons.io.FileUtils.readLines;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class NokeeGradleWrapperIntegrationFunctionalTest {
    @TempDir
    Path testDirectory;

    private void succeeds(String... args) throws IOException {
        val initScriptFile = testDirectory.resolve("init.gradle").toFile();
        FileUtils.write(initScriptFile, configurePluginClasspathAsInitScriptDependencies(), UTF_8);
        FileUtils.write(initScriptFile, "\napply plugin: dev.nokee.init.NokeeInitPlugin\n", UTF_8, true);
        GradleRunner.create(GradleExecutor.gradleTestKit()).inDirectory(testDirectory.toFile()).withArguments(args).usingInitScript(initScriptFile).build();
    }

    private static File getGoldNokeeInitScriptFile() {
        return new File(System.getProperty("dev.nokee.init.gold-nokee-init-script"));
    }

    @Test
    void generateGradleWrapper() throws IOException {
        succeeds("wrapper", "-Dnokee-version=0.4.0");
        wroteNokeeInitScript();
    }

    private void wroteNokeeInitScript() throws IOException {
        val initScriptFile = testDirectory.resolve("gradle/nokee.init.gradle").toFile();
        assertThat("gradle/nokee.init.gradle should exists", initScriptFile, FileMatchers.anExistingFile());
        assertEquals(readLines(initScriptFile, UTF_8), readLines(getGoldNokeeInitScriptFile(), UTF_8), "gradle/nokee.init.gradle should be the same as the gold init script");
    }
}
