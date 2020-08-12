package dev.nokee.init.internal;

import org.gradle.api.DefaultTask;
import org.gradle.api.file.ProjectLayout;
import org.gradle.api.provider.Property;
import org.gradle.api.provider.ProviderFactory;
import org.gradle.api.tasks.Internal;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.options.Option;

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
                     new ShowVersionCommand(new DefaultConsolePrinter(), new ProjectNokeeVersionProvider(getLayout().getProjectDirectory()::getAsFile)).run();
              }

              if (getNokeeVersion().isPresent()) {
                     new ConfigureNokeeVersionCommand(() -> getNokeeVersion().get(), new ProjectNokeeVersionWriter(getLayout().getProjectDirectory()::getAsFile)).run();
              }
       }
}
