package dev.nokee.init.internal.wrapper;

import org.gradle.api.Action;
import org.gradle.api.Project;
import org.gradle.buildinit.plugins.WrapperPlugin;

public final class OnlyWhenWrapperPluginIsAppliedAction implements Action<Project> {
	private final Action<Project> delegate;

	public OnlyWhenWrapperPluginIsAppliedAction(Action<Project> delegate) {
		this.delegate = delegate;
	}

	@Override
	public void execute(Project project) {
		project.getPlugins().withType(WrapperPlugin.class, wrapperPlugin -> {
			delegate.execute(project);
		});
	}
}
