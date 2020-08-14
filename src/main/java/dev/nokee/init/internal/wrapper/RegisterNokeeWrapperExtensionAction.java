package dev.nokee.init.internal.wrapper;

import dev.nokee.init.internal.accessors.DefaultSystemPropertyAccessor;
import dev.nokee.init.internal.accessors.GradlePropertyAccessor;
import dev.nokee.init.internal.versions.CompositeNokeeVersionProvider;
import dev.nokee.init.internal.versions.DefaultGradlePropertyNokeeVersionProvider;
import dev.nokee.init.internal.versions.DefaultSystemPropertyNokeeVersionProvider;
import org.gradle.api.Action;
import org.gradle.api.file.ProjectLayout;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.wrapper.Wrapper;
import org.gradle.util.VersionNumber;

import java.util.Optional;

public final class RegisterNokeeWrapperExtensionAction implements Action<Wrapper> {
    private final ObjectFactory objects;
    private final ProjectLayout layout;
    private final GradlePropertyAccessor gradlePropertyAccessor;

    public RegisterNokeeWrapperExtensionAction(ObjectFactory objects, ProjectLayout layout, GradlePropertyAccessor gradlePropertyAccessor) {
        this.objects = objects;
        this.layout = layout;
        this.gradlePropertyAccessor = gradlePropertyAccessor;
    }

    @Override
    public void execute(Wrapper task) {
        Property<String> nokeeVersion = objects.property(String.class);
        RegularFileProperty nokeeInitScriptFile = objects.fileProperty().convention(layout.getProjectDirectory().file("gradle/nokee.init.gradle"));
        task.getExtensions().add("nokeeVersion", nokeeVersion);
        task.getExtensions().add("nokeeInitScriptFile", nokeeInitScriptFile);
        task.doLast(new WriteNokeeVersionConfigurationAction(new CompositeNokeeVersionProvider(new DefaultSystemPropertyNokeeVersionProvider(DefaultSystemPropertyAccessor.INSTANCE), new DefaultGradlePropertyNokeeVersionProvider(gradlePropertyAccessor), () -> Optional.ofNullable(nokeeVersion.getOrNull()).map(VersionNumber::parse)), task::getPropertiesFile, nokeeInitScriptFile.getAsFile()::get, task::getScriptFile));
    }


}
