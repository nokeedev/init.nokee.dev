package dev.nokee.init.internal.buildinit.generator;

import org.gradle.buildinit.plugins.internal.InitSettings;
import org.gradle.nativeplatform.platform.internal.DefaultNativePlatform;
import org.gradle.util.GradleVersion;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static dev.nokee.init.internal.buildinit.NamespaceBuilder.toNamespace;

public class TemplateBindings {
    public static final TemplateBindings INSTANCE = new TemplateBindings();
    Map<String, String> get(InitSettings settings) {
        if (settings == null || settings.getProjectName().isEmpty()) {
            throw new IllegalArgumentException("Project name cannot be empty for a C++ project");
        }

        Map<String, String> result = new LinkedHashMap<>();
        String now = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(new Date());
        result.put("genDate", now);
        result.put("genUser", System.getProperty("user.name"));
        result.put("genGradleVersion", GradleVersion.current().toString());

        result.put("projectName", settings.getProjectName());
        result.put("gradleVersion", GradleVersion.current().getVersion());

        String namespace = toNamespace(settings.getProjectName());

        result.put("targetMachineDefinition", getHostTargetMachineDefinition());
        result.put("namespace", namespace);
        return result;
    }

    protected String getHostTargetMachineDefinition() {
        DefaultNativePlatform host = DefaultNativePlatform.host();
        String definition = "machines.";

        if (host.getOperatingSystem().isWindows()) {
            definition += "windows";
        } else if (host.getOperatingSystem().isMacOsX()) {
            definition += "macOS";
        } else if (host.getOperatingSystem().isLinux()) {
            definition += "linux";
        } else {
            definition += "os('" + host.getOperatingSystem().toFamilyName() + "')";
        }

        definition += ".";

        if (host.getArchitecture().isI386()) {
            definition += "x86";
        } else if (host.getArchitecture().isAmd64()) {
            definition += "x86_64";
        } else {
            definition += "architecture('" + host.getArchitecture().getName() + "')";
        }

        return definition;
    }
}
