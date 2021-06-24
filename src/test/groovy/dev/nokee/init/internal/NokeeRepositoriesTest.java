package dev.nokee.init.internal;

import org.gradle.api.Project;
import org.gradle.api.artifacts.repositories.MavenArtifactRepository;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static dev.nokee.init.internal.NokeeRepositories.releaseRepository;
import static dev.nokee.init.internal.NokeeRepositories.snapshotRepository;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class NokeeRepositoriesTest {
	private final Project project = ProjectBuilder.builder().build();

	@Nested
	class ReleaseRepository {
		private final MavenArtifactRepository subject = project.getRepositories().maven(releaseRepository());

		@Test
		void hasSensibleName() {
			assertThat(subject.getName(), equalTo("Nokee Release Artifacts Repository"));
		}

		@Test
		void useReleaseRepositoryUrl() {
			assertThat(subject.getUrl().toString(), equalTo("https://repo.nokee.dev/release"));
		}
	}

	@Nested
	class SnapshotRepository {
		private final MavenArtifactRepository subject = project.getRepositories().maven(snapshotRepository());

		@Test
		void hasSensibleName() {
			assertThat(subject.getName(), equalTo("Nokee Snapshot Artifacts Repository"));
		}

		@Test
		void useSnapshotRepositoryUrl() {
			assertThat(subject.getUrl().toString(), equalTo("https://repo.nokee.dev/snapshot"));
		}
	}
}
