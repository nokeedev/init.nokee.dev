package dev.nokee.init.internal.buildinit.descriptor;

import dev.nokee.init.internal.buildinit.NokeeLanguages;
import org.gradle.buildinit.plugins.internal.modifiers.ComponentType;
import org.gradle.buildinit.plugins.internal.modifiers.Language;

public final class CApplicationDescriptor implements NokeeProjectInitDescriptor {
    @Override
    public String getId() {
        return "nokee-c-application";
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.APPLICATION;
    }

    @Override
    public Language getLanguage() {
        return NokeeLanguages.C;
    }
}
