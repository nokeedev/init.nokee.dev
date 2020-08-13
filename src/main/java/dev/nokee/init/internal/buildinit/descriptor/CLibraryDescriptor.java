package dev.nokee.init.internal.buildinit.descriptor;

import dev.nokee.init.internal.buildinit.NokeeLanguages;
import org.gradle.buildinit.plugins.internal.modifiers.ComponentType;
import org.gradle.buildinit.plugins.internal.modifiers.Language;

public class CLibraryDescriptor implements NokeeProjectInitDescriptor {
    @Override
    public String getId() {
        return "nokee-c-library";
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.LIBRARY;
    }

    @Override
    public Language getLanguage() {
        return NokeeLanguages.C;
    }
}
