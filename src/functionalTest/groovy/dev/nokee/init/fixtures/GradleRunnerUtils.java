package dev.nokee.init.fixtures;

import dev.gradleplugins.integtests.fixtures.AbstractGradleSpecification;
import lombok.val;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class GradleRunnerUtils {
    private GradleRunnerUtils() {}

    public static String configurePluginClasspathAsInitScriptDependencies() throws IOException {
        return configureInitScriptDependencies(getImplementationClassPath());
    }

    private static Iterable<File> getImplementationClassPath() throws IOException {
        val properties = new Properties();
        properties.load(GradleRunnerUtils.class.getResourceAsStream("/plugin-under-test-metadata.properties"));
        return Arrays.stream(properties.get("implementation-classpath").toString().split(File.pathSeparator)).map(File::new).collect(Collectors.toList());
    }

    private static String configureInitScriptDependencies(Iterable<File> classpath) {
        val content = new StringWriter();
        val out = new PrintWriter(content);
        out.println("initscript {");
        out.println("    dependencies {");
        out.println("        classpath(files(" + asQuotedAbsolutePathSpread(classpath) + "))");
        out.println("    }");
        out.println("}");
        return content.toString();
    }

    private static String asQuotedAbsolutePathSpread(Iterable<File> classpath) {
        return StreamSupport.stream(classpath.spliterator(), false).map(quoted(File::getAbsolutePath)).collect(Collectors.joining(", "));
    }

    private static <T> Function<T, String> quoted(Function<T, String> mapper) {
        return t -> "\"" + mapper.apply(t) + "\"";
    }
}
