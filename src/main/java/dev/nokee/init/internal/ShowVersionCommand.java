package dev.nokee.init.internal;

import org.gradle.util.VersionNumber;

import java.util.Optional;
import java.util.function.Supplier;

public final class ShowVersionCommand implements Runnable {
    public static final String FLAG = "version";
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