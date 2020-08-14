package dev.nokee.init.internal.wrapper;

import dev.nokee.init.internal.utils.FileUtils;
import dev.nokee.init.internal.versions.NokeeVersionProvider;
import org.gradle.api.Action;
import org.gradle.api.Task;
import org.gradle.util.VersionNumber;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Objects;
import java.util.Properties;
import java.util.function.Supplier;

public final class WriteNokeeVersionConfigurationAction implements Action<Task> {
    private final NokeeVersionProvider nokeeVersionProvider;
    private final Supplier<File> propertiesFile;
    private final Supplier<File> initScriptFile;
    private final Supplier<File> scriptFile;

    public WriteNokeeVersionConfigurationAction(NokeeVersionProvider nokeeVersionProvider, Supplier<File> propertiesFile, Supplier<File> initScriptFile, Supplier<File> scriptFile) {
        this.nokeeVersionProvider = nokeeVersionProvider;
        this.propertiesFile = propertiesFile;
        this.initScriptFile = initScriptFile;
        this.scriptFile = scriptFile;
    }

    @Override
    public void execute(Task task) {
        nokeeVersionProvider.get().ifPresent(nokeeVersion -> {
            updateWrapperProperties(nokeeVersion);
            writeInitScriptFile();
            patchWrapperScriptFiles(nokeeVersion);
        });
    }

    private void patchWrapperScriptFiles(VersionNumber nokeeVersion) {
        String pathToInitScript = scriptFile.get().getParentFile().toPath().relativize(initScriptFile.get().toPath()).toString();

        File bashScriptFile = scriptFile.get();
        try {
            String content = FileUtils.readFileToString(bashScriptFile, Charset.defaultCharset());
            if (!content.contains("\"$APP_ARGS\"")) {
                throw new IllegalStateException(String.format("Could not find patching hook inside script at '%s'.", bashScriptFile.getAbsolutePath()));
            }
            content = content.replace("\"$APP_ARGS\"", "\"$APP_ARGS\" --init-script \"\\\"$APP_HOME/" + pathToInitScript + "\\\"\" -DuseNokeeVersionFromWrapper=" + nokeeVersion.toString());
            FileUtils.write(bashScriptFile, content, Charset.defaultCharset());
        } catch (IOException e) {
            throw new UncheckedIOException(String.format("Could not patch wrapper script at '%s'.", bashScriptFile.getAbsolutePath()), e);
        }

        File batchScriptFile = new File(scriptFile.get().getAbsolutePath() + ".bat");
        try {
            String content = FileUtils.readFileToString(batchScriptFile, Charset.defaultCharset());
            if (!content.contains("%CMD_LINE_ARGS%")) {
                throw new IllegalStateException(String.format("Could not find patching hook inside script at '%s'.", batchScriptFile.getAbsolutePath()));
            }
            content = content.replace("%CMD_LINE_ARGS%", "%CMD_LINE_ARGS% --init-script \"%APP_HOME%\\" + pathToInitScript.replace('/', '\\') + "\" -DuseNokeeVersionFromWrapper=" + nokeeVersion.toString());
            FileUtils.write(batchScriptFile, content, Charset.defaultCharset());
        } catch (IOException e) {
            throw new UncheckedIOException(String.format("Could not patch wrapper script at '%s'.", batchScriptFile.getAbsolutePath()), e);
        }
    }
    
    private void writeInitScriptFile() {
        InputStream inStream = this.getClass().getResourceAsStream("nokee.init.gradle");
        Objects.requireNonNull(inStream, "Could not find the Nokee init script inside the classpath.");

        try {
            Files.copy(inStream, initScriptFile.get().toPath());
        } catch (IOException e) {
            throw new UncheckedIOException(String.format("Could not write Nokee init script file at '%s'.", initScriptFile.get().getAbsolutePath()), e);
        } finally {
            closeQuietly(inStream);
        }
    }

    private static void closeQuietly(Closeable closeable) {
        try {
            closeable.close();
        } catch (IOException e) {
            // ignore
        }
    }

    private void updateWrapperProperties(VersionNumber nokeeVersion) {
        Properties properties = new Properties();

        readWrapperProperties(properties);

        properties.put("useNokeeVersion", nokeeVersion.toString());

        writeWrapperProperties(properties);
    }

    private void readWrapperProperties(Properties properties) {
        try (InputStream inStream = new FileInputStream(propertiesFile.get())) {
            properties.load(inStream);
        } catch (IOException e) {
            throw new UncheckedIOException(String.format("Could not read the wrapper properties file at '%s'.", propertiesFile.get().getAbsolutePath()), e);
        }
    }

    private void writeWrapperProperties(Properties properties) {
        try (OutputStream outStream = new FileOutputStream(propertiesFile.get())) {
            properties.store(outStream, null);
        } catch (IOException e) {
            throw new UncheckedIOException(String.format("Could not write the wrapper properties file at '%s'.", propertiesFile.get().getAbsolutePath()), e);
        }
    }
}
