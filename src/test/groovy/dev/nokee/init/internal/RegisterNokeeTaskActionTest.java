package dev.nokee.init.internal;

import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class RegisterNokeeTaskActionTest {
	private final Project project = ProjectBuilder.builder().build();

	@BeforeEach
	void executesAction() {
		new RegisterNokeeTaskAction().execute(project);
	}

	@Test
	void registerNokeeTask() {
		assertThat(getNokeeTask(), allOf(notNullValue(), isA(NokeeTask.class)));
	}

	@Test
	void hasGroup() {
		assertThat(getNokeeTask().getGroup(), equalTo("Build Setup"));
	}

	@Test
	void hasDescription() {
		assertThat(getNokeeTask().getDescription(), equalTo("Configures Gradle integration with Nokee plugins."));
	}

	private NokeeTask getNokeeTask() {
		return (NokeeTask) project.getTasks().findByName("nokee");
	}
}
