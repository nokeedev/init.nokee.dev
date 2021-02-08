package dev.nokee.init.internal;

import dev.nokee.init.internal.versions.BuildClasspathNokeeVersionProvider;
import org.gradle.api.Action;
import org.gradle.api.Describable;
import org.gradle.api.provider.Property;
import org.gradle.api.provider.ValueSource;
import org.gradle.api.provider.ValueSourceParameters;
import org.gradle.api.provider.ValueSourceSpec;

import javax.annotation.Nullable;
import java.util.stream.Collectors;

public final class ConfigureNokeeTaskAction implements Action<NokeeTask> {
	private final NokeeExtension extension;

	public ConfigureNokeeTaskAction(NokeeExtension extension) {
		this.extension = extension;
	}

	@Override
	public void execute(NokeeTask task) {
		task.setDescription("Configures Gradle integration with Nokee plugins.");
		task.setGroup("Build Setup");
		task.getCurrentVersion().set(extension.getVersion().orElse(task.getProviders().provider(() -> new BuildClasspathNokeeVersionProvider(task.getProject()).get().orElse(null))));

		// TODO: depends on included build :nokee tasks
		task.dependsOn(task.getProject().getGradle().getIncludedBuilds().stream().map(it -> {
			return it.task(":nokee");
		}).collect(Collectors.toList()));
		task.getShowVersion().convention(task.getProviders().of(SystemPropertyValueSource.class, new Action<ValueSourceSpec<AbstractPropertyValueSource.Parameters>>() {
			@Override
			public void execute(ValueSourceSpec<AbstractPropertyValueSource.Parameters> p) {
				p.getParameters().getPropertyName().set("show-version");
			}
		}));
		task.getBuildIdentityPath().convention(extension.getIdentityPath()).disallowChanges();
	}

	public static abstract class AbstractPropertyValueSource implements ValueSource<Boolean, AbstractPropertyValueSource.Parameters>, Describable {

		public interface Parameters extends ValueSourceParameters {
			Property<String> getPropertyName();
		}

		@Nullable
		@Override
		public abstract Boolean obtain();

		@Override
		public abstract String getDisplayName();

		@Nullable
		protected String propertyNameOrNull() {
			return getParameters().getPropertyName().getOrNull();
		}
	}

	public static abstract class SystemPropertyValueSource extends AbstractPropertyValueSource {

		@Nullable
		@Override
		public Boolean obtain() {
			@Nullable String propertyName = propertyNameOrNull();
			if (propertyName == null) {
				return null;
			}
			System.out.println("YOOOO!!!! " + System.getProperties().containsKey(propertyName));
			return System.getProperties().containsKey(propertyName);
		}

		@Override
		public String getDisplayName() {
			return String.format("system property '%s'", propertyNameOrNull());
		}
	}
}
