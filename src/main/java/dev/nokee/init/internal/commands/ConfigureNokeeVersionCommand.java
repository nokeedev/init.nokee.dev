package dev.nokee.init.internal.commands;

import dev.nokee.init.internal.NokeeVersionWriter;
import org.gradle.util.VersionNumber;

import java.util.Optional;
import java.util.function.Supplier;

public final class ConfigureNokeeVersionCommand implements Runnable {
    public static final String FLAG = "use-version";
    public static final String HELP_MESSAGE = "Specifies the nokee version to use in this project.";
    private final Supplier<String> nokeeVersion;
    private final NokeeVersionWriter writer;

    public ConfigureNokeeVersionCommand(Supplier<String> nokeeVersion, NokeeVersionWriter writer) {
        this.nokeeVersion = nokeeVersion;
        this.writer = writer;
    }

    @Override
    public void run() {
        Optional.ofNullable(nokeeVersion.get()).ifPresent(version -> {
            writer.write(VersionNumber.parse(version));
        });
    }
}
