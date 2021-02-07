package dev.nokee.init.internal.wrapper;

import com.google.common.testing.EqualsTester;
import lombok.val;
import org.junit.jupiter.api.Test;

import static dev.nokee.init.internal.wrapper.GradleWrapperScriptTestUtils.script;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GradleWrapperScriptTest {
    private static final String BASH_PATCHABLE_SCRIPT_LINE = "eval set -- $DEFAULT_JVM_OPTS $JAVA_OPTS $GRADLE_OPTS \"\\\"-Dorg.gradle.appname=$APP_BASE_NAME\\\"\" -classpath \"\\\"$CLASSPATH\\\"\" org.gradle.wrapper.GradleWrapperMain \"$APP_ARGS\"";
    private static final String BATCH_PATCHABLE_SCRIPT_LINE_6_6_ABOVE = "\"%JAVA_EXE%\" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %GRADLE_OPTS% \"-Dorg.gradle.appname=%APP_BASE_NAME%\" -classpath \"%CLASSPATH%\" org.gradle.wrapper.GradleWrapperMain %*";
    private static final String BATCH_PATCHABLE_SCRIPT_LINE_6_5_BELOW = "\"%JAVA_EXE%\" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %GRADLE_OPTS% \"-Dorg.gradle.appname=%APP_BASE_NAME%\" -classpath \"%CLASSPATH%\"     org.gradle.launcher.GradleMain %CMD_LINE_ARGS%";

    @Test
    void canPatchBashScript() {
        assertThat(script(BASH_PATCHABLE_SCRIPT_LINE).patch("gradle/nokee.init.gradle").get(),
                containsString(" --init-script \"\\\"$APP_HOME/gradle/nokee.init.gradle\\\"\" "));
    }

    @Test
    void canPatchBatchScriptForGradle6_5Below() {
        assertThat(script(BATCH_PATCHABLE_SCRIPT_LINE_6_5_BELOW).patch("gradle/nokee.init.gradle").get(),
                containsString(" --init-script \"%APP_HOME%\\gradle\\nokee.init.gradle\" "));
    }

    @Test
    void canPatchBatchScriptForGradle6_6Above() {
        assertThat(script(BATCH_PATCHABLE_SCRIPT_LINE_6_6_ABOVE).patch("gradle/nokee.init.gradle").get(),
                containsString(" --init-script \"%APP_HOME%\\gradle\\nokee.init.gradle\" "));
    }

    @Test
    void throwsExceptionWhenScriptDoesNotHavePatchableHook() {
        val ex = assertThrows(IllegalStateException.class, () -> script("some content without patchable hook").patch("gradle/nokee.init.gradle"));
        assertThat(ex.getMessage(), equalTo("Could not find patching hook inside Gradle wrapper script."));
    }

    @Test
    @SuppressWarnings("UnstableApiUsage")
    void checkEquals() {
        new EqualsTester().addEqualityGroup(new GradleWrapperScript("foo"), new GradleWrapperScript("foo")).addEqualityGroup(new GradleWrapperScript("bar")).testEquals();
    }
}
