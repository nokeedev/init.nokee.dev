package dev.nokee.init.internal.wrapper;

public final class GradleWrapperScriptTestUtils {
	private GradleWrapperScriptTestUtils() {}

	public static GradleWrapperScript script(String... line) {
		return new GradleWrapperScript(String.join("\n", line));
	}
}
