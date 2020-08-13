package dev.nokee.init.internal.buildinit;

import dev.nokee.init.internal.buildinit.descriptor.NokeeProjectInitDescriptor;
import org.gradle.buildinit.plugins.internal.BuildContentGenerator;
import org.gradle.buildinit.plugins.internal.BuildInitializer;
import org.gradle.buildinit.plugins.internal.InitSettings;
import org.gradle.buildinit.plugins.internal.modifiers.BuildInitDsl;
import org.gradle.buildinit.plugins.internal.modifiers.BuildInitTestFramework;
import org.gradle.buildinit.plugins.internal.modifiers.ComponentType;
import org.gradle.buildinit.plugins.internal.modifiers.Language;

import java.util.*;

public final class NokeeBuildInitializer implements BuildInitializer {

    private final NokeeProjectInitDescriptor descriptor;
    private final List<BuildContentGenerator> contentGenerators;

    protected NokeeBuildInitializer(NokeeProjectInitDescriptor descriptor, BuildContentGenerator... contentGenerators) {
        this.descriptor = descriptor;
        this.contentGenerators = Arrays.asList(contentGenerators);
    }

    @Override
    public Optional<String> getFurtherReading() {
        return Optional.empty();
    }

    @Override
    public Set<BuildInitTestFramework> getTestFrameworks() {
        // Because BuildInitTestFramework is a static enum, we are going to provide multiple init per language.
        return Collections.emptySet();
    }

    @Override
    public BuildInitTestFramework getDefaultTestFramework() {
        return null;
    }

    @Override
    public boolean supportsPackage() {
        return false;
    }

    @Override
    public String getId() {
        return descriptor.getId();
    }

    @Override
    public ComponentType getComponentType() {
        return descriptor.getComponentType();
    }

    @Override
    public Language getLanguage() {
        return descriptor.getLanguage();
    }

    @Override
    public Set<BuildInitDsl> getDsls() {
        return descriptor.getDsls();
    }

    @Override
    public BuildInitDsl getDefaultDsl() {
        return descriptor.getDefaultDsl();
    }

    @Override
    public boolean supportsProjectName() {
        return true;
    }

    @Override
    public void generate(InitSettings initSettings) {
        contentGenerators.forEach(it -> it.generate(initSettings));
    }
}
