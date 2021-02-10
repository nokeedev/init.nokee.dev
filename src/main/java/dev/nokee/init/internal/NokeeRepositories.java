package dev.nokee.init.internal;

import org.gradle.api.Action;
import org.gradle.api.artifacts.repositories.MavenArtifactRepository;

public final class NokeeRepositories {
	private NokeeRepositories() {}

	public static Action<MavenArtifactRepository> releaseRepository() {
		return repository -> {
			repository.setName("Nokee Release Artifacts Repository");
			repository.setUrl("https://repo.nokeedev.net/release");
			repository.mavenContent(content -> {
				content.includeGroupByRegex("dev\\.nokee.*");
				content.includeGroupByRegex("dev\\.gradleplugins.*");
			});
		};
	}

	public static Action<MavenArtifactRepository> snapshotRepository() {
		return repository -> {
			repository.setName("Nokee Snapshot Artifacts Repository");
			repository.setUrl("https://repo.nokeedev.net/snapshot");
			repository.mavenContent(content -> {
				content.includeGroupByRegex("dev\\.nokee.*");
				content.includeGroupByRegex("dev\\.gradleplugins.*");
			});
		};
	}
}
