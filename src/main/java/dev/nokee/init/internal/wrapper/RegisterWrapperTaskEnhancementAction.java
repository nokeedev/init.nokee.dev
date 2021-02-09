package dev.nokee.init.internal.wrapper;

import dev.nokee.init.internal.NokeeExtension;
import dev.nokee.init.internal.versions.BuildClasspathNokeeVersionProvider;
import dev.nokee.init.internal.versions.DefaultNokeeVersionProviderFactory;
import dev.nokee.init.internal.versions.DefaultNokeeVersionWrapperOverrideProvider;
import lombok.val;
import org.gradle.api.Action;
import org.gradle.api.Project;
import org.gradle.api.specs.Spec;
import org.gradle.api.tasks.wrapper.Wrapper;
import org.gradle.buildinit.plugins.WrapperPlugin;

public final class RegisterWrapperTaskEnhancementAction implements Action<Project> {
	@Override
	public void execute(Project project) {
		project.getPlugins().withType(WrapperPlugin.class, wrapperPlugin -> {
			val extensionProvider = project.provider(() -> project.getExtensions().getByType(NokeeExtension.class));
			project.getTasks().named("wrapper", Wrapper.class, task -> {
				// Register Nokee extensions
				val extension = project.getObjects().newInstance(NokeeWrapperExtension.class);
				task.getExtensions().add("nokee", extension);

				// Configure defaults
				extension.getVersion().convention(extensionProvider.flatMap(it -> it.getVersion().orElse(project.provider(() -> new BuildClasspathNokeeVersionProvider(task.getProject()).get().orElse(null)))).map(it -> it.get().toString())).finalizeValueOnRead();
				extension.getInitScriptFile().convention(project.getLayout().getProjectDirectory().file("gradle/nokee.init.gradle")).finalizeValueOnRead();

				// TODO: Here were are using the version provider factory, etc. But in fact it's more a CLI flag provider
				val nokeeWrapperVersion = project.provider(new DefaultNokeeVersionWrapperOverrideProvider(new DefaultNokeeVersionProviderFactory(project.getProviders(), null))).map(it -> it.get().toString()).orElse(extension.getVersion());

				// Mark additional inputs/outputs
				task.getInputs().property("nokeeVersion", nokeeWrapperVersion).optional(true);
				task.getOutputs().file(nokeeWrapperVersion.map(it -> extension.getInitScriptFile().get())).optional();

				// Add enhancement actions
				task.doLast(onlyIf(it -> nokeeWrapperVersion.isPresent(),
					new GradleWrapperActionNokeeInitScriptWriter(extension.getInitScriptFile().getAsFile())));
				task.doLast(onlyIf(it -> nokeeWrapperVersion.isPresent(),
					new GradleWrapperActionNokeeVersionPropertyWriter(nokeeWrapperVersion, project.provider(() -> task.getPropertiesFile()))));
				task.doLast(onlyIf(it -> nokeeWrapperVersion.isPresent(),
					new GradleWrapperActionScriptPatcher(project.provider(() -> task.getScriptFile()), extension.getInitScriptFile().getAsFile())));
			});
		});
	}

	private static <T> Action<T> onlyIf(Spec<T> spec, Action<T> action) {
		return new Action<T>() {
			@Override
			public void execute(T t) {
				if (spec.isSatisfiedBy(t)) {
					action.execute(t);
				}
			}
		};
	}
}
