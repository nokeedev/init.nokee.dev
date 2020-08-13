package dev.nokee.init.internal.buildinit.generator;

import org.gradle.api.file.Directory;
import org.gradle.api.file.RegularFile;
import org.gradle.buildinit.plugins.internal.*;
import org.gradle.util.GradleVersion;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class NokeeTemplateGenerator implements BuildContentGenerator {
    private final String relativeTemplatePath;
    private final File targetFile;
    private final TemplateBindings bindings;

    public NokeeTemplateGenerator(String relativeTemplatePath, File targetFile, TemplateBindings bindings) {
        this.relativeTemplatePath = relativeTemplatePath;
        this.targetFile = targetFile;
        this.bindings = bindings;
    }

    @Override
    public void generate(InitSettings initSettings) {
        final Set<Map.Entry<String, String>> entries = bindings.get(initSettings).entrySet();
        Map<String, TemplateValue> wrappedBindings = new HashMap<>(entries.size());
        for (Map.Entry<String, String> entry : entries) {
            if (entry.getValue() == null) {
                throw new IllegalArgumentException("Null value provided for binding '" + entry.getKey() + "'.");
            }
            wrappedBindings.put(entry.getKey(), new TemplateValue(entry.getValue()));
        }
        new SimpleTemplateOperation(toUrl(relativeTemplatePath), targetFile, wrappedBindings).generate();
    }
//
//    TemplateOperation fromTemplate(InitSettings settings, String template, String targetFilePath) {
//        return fromTemplate(toUrl(template), targetFilePath, bindings.get(settings));
//    }
//
//    TemplateOperation fromTemplate(String template, String targetFilePath, Map<String, String> bindings) {
//        return fromTemplate(toUrl(template), targetFilePath, bindings);
//    }
//
//    TemplateOperation fromTemplate(URL templateUrl, String targetFilePath, Map<String, String> bindings) {
//        TemplateOperationFactory.TemplateOperationBuilder builder = templateOperationFactory.newTemplateOperation()
//                .withTemplate(templateUrl)
//                .withTarget(targetFilePath);
//        bindings.forEach(builder::withBinding);
//        return builder.create();
//    }
//
    URL toUrl(String relativeTemplatePath) {
        URL templateUrl = getClass().getResource("/dev/nokee/init/internal/buildinit/templates/" + relativeTemplatePath);
        if (templateUrl == null) {
            throw new IllegalArgumentException(String.format("Could not find template '%s' in classpath.", relativeTemplatePath));
        }
        return templateUrl;
    }
}
