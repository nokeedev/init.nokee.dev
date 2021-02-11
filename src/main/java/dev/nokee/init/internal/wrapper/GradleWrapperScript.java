package dev.nokee.init.internal.wrapper;

import lombok.EqualsAndHashCode;

import java.util.Arrays;
import java.util.List;

import static dev.nokee.init.internal.utils.FilenameUtils.separatorsToUnix;
import static dev.nokee.init.internal.utils.FilenameUtils.separatorsToWindows;
import static dev.nokee.init.internal.wrapper.GradleWrapperScriptPatcher.forContent;
import static java.lang.String.join;

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
			return new GradleWrapperScript(join("\n", forContent(content).findLineWith("\"$APP_ARGS\"").injectBefore(bashScriptPath(pathToInitScript)).andPrependPillWith("\"$NOKEE_ARGS\" ").patch()));
		}

		// Batch script 6.6 and above
		if (content.contains("%*")) {
			return new GradleWrapperScript(join("\r\n", forContent(content).findLineWith("%*").injectBefore(batchScriptPath(pathToInitScript)).andPrependPillWith("%NOKEE_ARGS% ").patch()));
		}

		// Batch script 6.5 and lower
		if (content.contains("%CMD_LINE_ARGS%")) {
			return new GradleWrapperScript(join("\r\n", forContent(content).findLineWith("%CMD_LINE_ARGS%").injectBefore(batchScriptPath(pathToInitScript)).andPrependPillWith("%NOKEE_ARGS% ").patch()));
		}

		throw new IllegalStateException("Could not find patching hook inside Gradle wrapper script.");
	}

	private static List<String> bashScriptPath(String pathToInitScript) {
		return Arrays.asList(
			"NOKEE_INIT_SCRIPT_FILE=$APP_HOME/" + separatorsToUnix(pathToInitScript),
			"if [ -f \"$NOKEE_INIT_SCRIPT_FILE\" ] ; then",
			"    NOKEE_ARGS=\"--init-script \\\"$NOKEE_INIT_SCRIPT_FILE\\\"\"",
			"fi",
			"");
	}

	private static List<String> batchScriptPath(String pathToInitScript) {
		return Arrays.asList(
			"set NOKEE_INIT_SCRIPT_FILE=%APP_HOME%\\" + separatorsToWindows(pathToInitScript),
			"if exist NOKEE_INIT_SCRIPT_FILE set NOKEE_ARGS=--init-script \"%NOKEE_INIT_SCRIPT_FILE%\"",
			"");
	}
}
