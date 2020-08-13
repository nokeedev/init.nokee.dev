package dev.nokee.init.internal.buildinit.descriptor;

import dev.nokee.init.internal.buildinit.NokeeLanguages;
import org.gradle.buildinit.plugins.internal.modifiers.ComponentType;
import org.gradle.buildinit.plugins.internal.modifiers.Language;

public class WithGoogleTestDescriptor implements NokeeProjectInitDescriptor {
    private final NokeeProjectInitDescriptor delegate;

    public WithGoogleTestDescriptor(NokeeProjectInitDescriptor delegate) {
        this.delegate = delegate;
    }

    @Override
    public String getId() {
        return delegate.getId() + "-with-google-test";
    }

    @Override
    public ComponentType getComponentType() {
        return delegate.getComponentType();
    }

    @Override
    public Language getLanguage() {
        return NokeeLanguages.valueOf(delegate.getLanguage().getName() + "-with-google-test");
    }
}
