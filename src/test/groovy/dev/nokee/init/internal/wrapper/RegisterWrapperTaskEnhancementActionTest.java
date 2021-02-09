package dev.nokee.init.internal.wrapper;

import com.google.common.reflect.TypeToken;
import org.gradle.api.Project;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.wrapper.Wrapper;
import org.gradle.buildinit.plugins.WrapperPlugin;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.isA;

class RegisterWrapperTaskEnhancementActionTest {
	private final Project project = ProjectBuilder.builder().build();

	@BeforeEach
	void executesAction() {
		project.getPluginManager().apply(WrapperPlugin.class);
		new RegisterWrapperTaskEnhancementAction().execute(project);
	}

	@Test
	void registersEnhancementTaskActions() {
		System.out.println(getWrapperTask());
		assertThat(getWrapperTask().getActions(), hasSize(1 /* default */ + 3 /* enhancement */));
	}

	@Test
	void registersNokeeVersionExtension() {
		assertThat(getWrapperTask().getExtensions().findByName("nokee"), isA(NokeeWrapperExtension.class));
	}

	private Wrapper getWrapperTask() {
		return (Wrapper) project.getTasks().findByName("wrapper");
	}
}
