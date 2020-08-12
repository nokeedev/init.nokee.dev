package dev.nokee.init.internal;

import org.gradle.api.Action;
import org.gradle.plugin.management.PluginResolveDetails;
import org.gradle.util.VersionNumber;

public class AlignPluginNamespaceRequestToSpecificVersionAction implements Action<PluginResolveDetails> {
    private final String pluginNamespace;
    private final VersionNumber alignmentVersion;

    public AlignPluginNamespaceRequestToSpecificVersionAction(String pluginNamespace, VersionNumber alignmentVersion) {
        this.pluginNamespace = pluginNamespace;
        this.alignmentVersion = alignmentVersion;
    }

    @Override
    public void execute(PluginResolveDetails details) {
        if (details.getRequested().getId().getId().startsWith(pluginNamespace)) {
            details.useVersion(alignmentVersion.toString());
        }
    }
}
