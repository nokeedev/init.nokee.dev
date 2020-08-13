package dev.nokee.init.internal.buildinit.descriptor;

import dev.nokee.init.internal.buildinit.NokeeLanguages;
import org.gradle.buildinit.plugins.internal.modifiers.ComponentType;
import org.gradle.buildinit.plugins.internal.modifiers.Language;

public class CppApplicationDescriptor implements NokeeProjectInitDescriptor {
    private static final Language LANGUAGE = Language.withName("[nokee] C++", "cpp");

    @Override
    public String getId() {
        return "nokee-cpp-application";
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.APPLICATION;
    }

    @Override
    public Language getLanguage() {
        return NokeeLanguages.CPP;
    }
}
