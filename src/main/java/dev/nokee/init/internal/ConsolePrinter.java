package dev.nokee.init.internal;

public interface ConsolePrinter {
	ConsolePrinter println(String str);

	ConsolePrinter println();

	ConsolePrinter flush();
}
