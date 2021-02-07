package dev.nokee.init.internal.wrapper;

import lombok.EqualsAndHashCode;

import static dev.nokee.init.internal.utils.FilenameUtils.separatorsToUnix;
import static dev.nokee.init.internal.utils.FilenameUtils.separatorsToWindows;

@EqualsAndHashCode
public final class GradleWrapperScript {
    private final String content;

    public GradleWrapperScript(String content) {
        this.content = content;
    }

    public String get() {
        return content;
    }

    public GradleWrapperScript patch(String pathToInitScript) {
        // Bash script
        if (content.contains("\"$APP_ARGS\"")) {
            return new GradleWrapperScript(content.replace("\"$APP_ARGS\"","--init-script \"\\\"$APP_HOME/" + separatorsToUnix(pathToInitScript) + "\\\"\" \"$APP_ARGS\""));
        }

        // Batch script 6.6 and above
        if (content.contains("%*")) {
            return new GradleWrapperScript(content.replace("%*", "--init-script \"%APP_HOME%\\" + separatorsToWindows(pathToInitScript) + "\" %*"));
        }

        // Batch script 6.5 and lower
        if (content.contains("%CMD_LINE_ARGS%")) {
            return new GradleWrapperScript(content.replace("%CMD_LINE_ARGS%", "--init-script \"%APP_HOME%\\" + separatorsToWindows(pathToInitScript) + "\" %CMD_LINE_ARGS%"));
        }

        throw new IllegalStateException("Could not find patching hook inside Gradle wrapper script.");
    }
}
