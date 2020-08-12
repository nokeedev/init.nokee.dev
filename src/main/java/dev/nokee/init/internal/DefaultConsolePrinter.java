package dev.nokee.init.internal;

import java.io.OutputStream;
import java.io.PrintWriter;

public final class DefaultConsolePrinter implements ConsolePrinter {
    private final PrintWriter writer;

    public DefaultConsolePrinter() {
        this(System.out);
    }

    public DefaultConsolePrinter(OutputStream outStream) {
        this.writer = new PrintWriter(outStream);
    }

    @Override
    public ConsolePrinter println(String str) {
        writer.println(str);
        return this;
    }

    @Override
    public ConsolePrinter println() {
        writer.println();
        return this;
    }

    @Override
    public ConsolePrinter flush() {
        writer.flush();
        return this;
    }
}
