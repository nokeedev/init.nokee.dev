package dev.nokee.init.internal.wrapper;

import org.gradle.api.Action;
import org.gradle.api.file.ProjectLayout;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.Property;
import org.gradle.api.provider.ProviderFactory;
import org.gradle.api.tasks.wrapper.Wrapper;

public final class RegisterNokeeWrapperExtensionAction implements Action<Wrapper> {
    private final ObjectFactory objects;
    private final ProviderFactory providers;
    private final ProjectLayout layout;

    public RegisterNokeeWrapperExtensionAction(ObjectFactory objects, ProviderFactory providers, ProjectLayout layout) {
        this.objects = objects;
        this.providers = providers;
        this.layout = layout;
    }

    @Override
    public void execute(Wrapper task) {
        Property<String> nokeeVersion = objects.property(String.class);
        RegularFileProperty nokeeInitScriptFile = objects.fileProperty().convention(layout.getProjectDirectory().file("gradle/nokee.init.gradle"));
        task.getExtensions().add("nokeeVersion", nokeeVersion);
        task.getExtensions().add("nokeeInitScriptFile", nokeeInitScriptFile);
        task.doLast(new WriteNokeeVersionConfigurationAction(new WrapperNokeeVersionProvider(nokeeVersion, providers), task::getPropertiesFile, nokeeInitScriptFile.getAsFile()::get, task::getScriptFile));
    }


}
