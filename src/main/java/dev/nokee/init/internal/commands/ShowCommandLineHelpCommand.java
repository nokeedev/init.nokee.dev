package dev.nokee.init.internal.commands;

import dev.nokee.init.internal.ConsolePrinter;

public final class ShowCommandLineHelpCommand implements Runnable {
    public static final String FLAG = "show-help";
    public static final String HELP_MESSAGE = "Shows help message.";
    private final ConsolePrinter out;

    public ShowCommandLineHelpCommand(ConsolePrinter out) {
        this.out = out;
    }

    @Override
    public void run() {
        out.println();
        out.println("USAGE: gradlew nokee [option...]");
        out.println();
        out.println(String.format("--%s\t\t\t%s", FLAG, HELP_MESSAGE));
        out.println(String.format("--%s\t\t%s", ShowVersionCommand.FLAG, ShowVersionCommand.HELP_MESSAGE));
        out.println(String.format("--%s\t\t%s", ConfigureNokeeVersionCommand.FLAG, ConfigureNokeeVersionCommand.HELP_MESSAGE));
        out.println();
        out.flush();
    }
}
