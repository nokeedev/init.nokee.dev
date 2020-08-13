package dev.nokee.init.internal.buildinit.descriptor;

import org.gradle.buildinit.plugins.internal.modifiers.BuildInitDsl;
import org.gradle.buildinit.plugins.internal.modifiers.ComponentType;
import org.gradle.buildinit.plugins.internal.modifiers.Language;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

public interface NokeeProjectInitDescriptor {
    Set<BuildInitDsl> SUPPORTED_DSLS = new TreeSet<>(Arrays.asList(BuildInitDsl.values()));

    String getId();

    ComponentType getComponentType();

    Language getLanguage();

    default Set<BuildInitDsl> getDsls() {
        return SUPPORTED_DSLS;
    }

    default BuildInitDsl getDefaultDsl() {
        return BuildInitDsl.GROOVY;
    }
}
