package dev.nokee.init.internal.buildinit;

import org.gradle.api.internal.DocumentationRegistry;

public class NokeeDocumentationRegistry extends DocumentationRegistry {

    @Override
    public String getDocumentationFor(String id) {
        return String.format("https://nokee.dev/docs/0.4.0/userguide/%s.html", id);
    }
}
