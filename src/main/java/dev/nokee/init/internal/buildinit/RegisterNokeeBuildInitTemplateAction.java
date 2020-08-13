package dev.nokee.init.internal.buildinit;

import dev.nokee.init.internal.buildinit.descriptor.CppApplicationDescriptor;
import dev.nokee.init.internal.buildinit.descriptor.CppLibraryDescriptor;
import dev.nokee.init.internal.buildinit.descriptor.WithGoogleTestDescriptor;
import dev.nokee.init.internal.buildinit.generator.NokeeTemplateGenerator;
import dev.nokee.init.internal.buildinit.generator.TemplateBindings;
import org.gradle.api.Action;
import org.gradle.api.Project;
import org.gradle.api.file.Directory;
import org.gradle.api.file.RegularFile;
import org.gradle.api.internal.DocumentationRegistry;
import org.gradle.api.internal.file.FileResolver;
import org.gradle.api.internal.project.ProjectInternal;
import org.gradle.buildinit.plugins.internal.BuildContentGenerator;
import org.gradle.buildinit.plugins.internal.GitAttributesGenerator;
import org.gradle.buildinit.plugins.internal.GitIgnoreGenerator;
import org.gradle.buildinit.plugins.internal.ProjectLayoutSetupRegistry;

public class RegisterNokeeBuildInitTemplateAction implements Action<Project> {
    private DocumentationRegistry documentationRegistry = new NokeeDocumentationRegistry();

    @Override
    public void execute(Project project) {
        ProjectInternal projectInternal = (ProjectInternal)project;
        ProjectLayoutSetupRegistry registry = projectInternal.getServices().get(ProjectLayoutSetupRegistry.class);
        FileResolver fileResolver = projectInternal.getFileResolver();

        Directory dir = project.getLayout().getProjectDirectory();

        // Common
        BuildContentGenerator gitIgnoreGenerator = new GitIgnoreGenerator(fileResolver);
        BuildContentGenerator gitAttributesGenerator = new GitAttributesGenerator(fileResolver);

        // C++ Application
        BuildContentGenerator cppAppHeaderGenerator = fromTemplate("cppapp/app.h.template", dir.file("src/main/headers/app.h"));
        BuildContentGenerator cppAppGenerator = fromTemplate("cppapp/app.cpp.template", dir.file("src/main/cpp/app.cpp"));

        // C++ Application with Google Test
        BuildContentGenerator cppAppGoogleTestGenerator = fromTemplate("cppapp/googletest/app_test.cpp.template", dir.file("src/test/cpp/app_test.cpp"));
        BuildContentGenerator cppAppBuildGoogleTestGenerator = fromTemplate("cppapp/googletest/build.gradle.template", dir.file("build.gradle"));
        BuildContentGenerator cppAppSettingsGoogleTestGenerator = fromTemplate("cppapp/googletest/settings.gradle.template", dir.file("settings.gradle"));

        // C++ Application with generic test
        BuildContentGenerator cppAppGenericTestGenerator = fromTemplate("cppapp/generic/app_test.cpp.template", dir.file("src/test/cpp/app_test.cpp"));
        BuildContentGenerator cppAppBuildGenericTestGenerator = fromTemplate("cppapp/generic/build.gradle.template", dir.file("build.gradle"));
        BuildContentGenerator cppAppSettingsGenericTestGenerator = fromTemplate("cppapp/generic/settings.gradle.template", dir.file("settings.gradle"));

        registry.add(new NokeeBuildInitializer(new CppApplicationDescriptor(), gitIgnoreGenerator, gitAttributesGenerator, cppAppHeaderGenerator, cppAppGenerator, cppAppGenericTestGenerator, cppAppBuildGenericTestGenerator, cppAppSettingsGenericTestGenerator));
        registry.add(new NokeeBuildInitializer(new WithGoogleTestDescriptor(new CppApplicationDescriptor()), gitIgnoreGenerator, gitAttributesGenerator, cppAppHeaderGenerator, cppAppGenerator, cppAppGoogleTestGenerator, cppAppBuildGoogleTestGenerator, cppAppSettingsGoogleTestGenerator));

        // C++ Application
        BuildContentGenerator cppLibHeaderGenerator = fromTemplate("cpplibrary/hello.h.template", dir.file("src/main/public/hello.h"));
        BuildContentGenerator cppLibGenerator = fromTemplate("cpplibrary/hello.cpp.template", dir.file("src/main/cpp/hello.cpp"));

        // C++ Application with Google Test
        BuildContentGenerator cppLibGoogleTestGenerator = fromTemplate("cpplibrary/googletest/hello_test.cpp.template", dir.file("src/test/cpp/hello_test.cpp"));
        BuildContentGenerator cppLibBuildGoogleTestGenerator = fromTemplate("cpplibrary/googletest/build.gradle.template", dir.file("build.gradle"));
        BuildContentGenerator cppLibSettingsGoogleTestGenerator = fromTemplate("cpplibrary/googletest/settings.gradle.template", dir.file("settings.gradle"));

        // C++ Application with generic test
        BuildContentGenerator cppLibGenericTestGenerator = fromTemplate("cpplibrary/generic/hello_test.cpp.template", dir.file("src/test/cpp/hello_test.cpp"));
        BuildContentGenerator cppLibBuildGenericTestGenerator = fromTemplate("cpplibrary/generic/build.gradle.template", dir.file("build.gradle"));
        BuildContentGenerator cppLibSettingsGenericTestGenerator = fromTemplate("cpplibrary/generic/settings.gradle.template", dir.file("settings.gradle"));

        registry.add(new NokeeBuildInitializer(new CppLibraryDescriptor(), gitIgnoreGenerator, gitAttributesGenerator, cppLibGenerator, cppLibHeaderGenerator, cppLibGenericTestGenerator, cppLibBuildGenericTestGenerator, cppLibSettingsGenericTestGenerator));
        registry.add(new NokeeBuildInitializer(new WithGoogleTestDescriptor(new CppLibraryDescriptor()), gitIgnoreGenerator, gitAttributesGenerator, cppLibGenerator, cppLibHeaderGenerator, cppLibGoogleTestGenerator, cppLibBuildGoogleTestGenerator, cppLibSettingsGoogleTestGenerator));
    }

    public BuildContentGenerator fromTemplate(String template, RegularFile targetFile) {
        return new NokeeTemplateGenerator(template, targetFile.getAsFile(), TemplateBindings.INSTANCE);
    }
}
