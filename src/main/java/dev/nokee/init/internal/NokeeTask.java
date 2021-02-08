package dev.nokee.init.internal;

import dev.nokee.init.internal.commands.ConfigureNokeeVersionCommand;
import dev.nokee.init.internal.commands.ShowCommandLineHelpCommand;
import dev.nokee.init.internal.commands.ShowVersionCommand;
import dev.nokee.init.internal.versions.NokeeVersion;
import org.gradle.api.DefaultTask;
import org.gradle.api.file.ProjectLayout;
import org.gradle.api.provider.Property;
import org.gradle.api.provider.ProviderFactory;
import org.gradle.api.tasks.Internal;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.options.Option;
import org.gradle.util.Path;

import javax.inject.Inject;
import java.util.stream.Stream;

public abstract class NokeeTask extends DefaultTask {
	@Inject
	public NokeeTask() {
		// Show help if task executes without option
		getShowHelp().convention(getProviders().provider(() -> Stream.of(getShowVersion().orElse(false), getNokeeVersion().map(it -> true).orElse(false)).allMatch(it -> !it.get())));
	}

	@Internal
	@Option(option = ShowCommandLineHelpCommand.FLAG, description = ShowCommandLineHelpCommand.HELP_MESSAGE)
	protected abstract Property<Boolean> getShowHelp();

	@Internal
	@Option(option = ShowVersionCommand.FLAG, description = ShowVersionCommand.HELP_MESSAGE)
	protected abstract Property<Boolean> getShowVersion();

	@Internal
	@Option(option = "use-version", description = "Configure nokee version to use in this project.")
	protected abstract Property<String> getNokeeVersion();

	@Internal
	protected abstract Property<Path> getBuildIdentityPath();

	@Internal
	protected abstract Property<NokeeVersion> getCurrentVersion();

	@Inject
	protected abstract ProjectLayout getLayout();

	@Inject
	protected abstract ProviderFactory getProviders();

	@TaskAction
	private void doAction() {
		if (getShowHelp().getOrElse(false)) {
			new ShowCommandLineHelpCommand(new DefaultConsolePrinter()).run();
		}

		if (getShowVersion().getOrElse(false)) {
			new ShowVersionCommand(new DefaultConsolePrinter(), getCurrentVersion(), getBuildIdentityPath()).run();
		}

		if (getNokeeVersion().isPresent()) {
			new ConfigureNokeeVersionCommand(() -> getNokeeVersion().get(), new ProjectNokeeVersionWriter(getLayout().getProjectDirectory()::getAsFile)).run();
		}
	}
}
