package dev.nokee.init.internal.commands;

import dev.nokee.init.internal.ConsolePrinter;
import dev.nokee.init.internal.versions.NokeeVersionProvider;
import org.gradle.util.VersionNumber;

import java.util.Optional;

public final class ShowVersionCommand implements Runnable {
    public static final String FLAG = "show-version";
    public static final String HELP_MESSAGE = "Print version info.";
    private final ConsolePrinter out;
    private final NokeeVersionProvider nokeeVersionProvider;

    public ShowVersionCommand(ConsolePrinter out, NokeeVersionProvider nokeeVersionProvider) {
        this.out = out;
        this.nokeeVersionProvider = nokeeVersionProvider;
    }

    @Override
    public void run() {
        Optional<VersionNumber> nokeeVersion = nokeeVersionProvider.get();
        if (nokeeVersion.isPresent()) {
            out.println("Using Nokee " + nokeeVersion.get().toString());
        } else {
            out.println("Nokee isn't configured for this project, please use ./gradlew nokee --use-version=<version>");
        }
        out.flush();
    }
}
