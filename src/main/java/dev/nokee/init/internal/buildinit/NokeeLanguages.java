package dev.nokee.init.internal.buildinit;

import org.gradle.buildinit.plugins.internal.modifiers.Language;

import java.util.Arrays;

public class NokeeLanguages {
    public static final Language CPP = Language.withName("[nokee] C++", "cpp");
    public static final Language C = Language.withName("[nokee] C", "c");
    private static final Language CPP_WITH_GOOGLE_TEST = Language.withName("[nokee] C++ with Google Test", "cpp-with-google-test");

    private static final Language[] VALUES = new Language[] {
            CPP, CPP_WITH_GOOGLE_TEST
    };

    public static Language valueOf(String name) {
        return Arrays.stream(VALUES).filter(it -> it.getName().equals(name)).findFirst().get();
    }
}
