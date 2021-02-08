package dev.nokee.init.internal.versions;

/**
 * Provides information where the init plugin sourced the Nokee version.
 */
enum NokeeVersionSource {
	GradleProperty {
		@Override
		public String toString() {
			return "Gradle property";
		}
	},
	SystemProperty {
		@Override
		public String toString() {
			return "System property";
		}
	},
	EnvironmentVariable {
		@Override
		public String toString() {
			return "environment variable";
		}
	},
	GradleWrapperProperty {
		@Override
		public String toString() {
			return "Gradle wrapper property";
		}
	},
	SettingsBuildscriptBlock {
		@Override
		public String toString() {
			return "buildscript classpath";
		}
	},
	BuildClasspath {
		@Override
		public String toString() {
			return "build classpath";
		}
	},
	CacheFile {
		@Override
		public String toString() {
			return "cache file";
		}
	},
	None {
		@Override
		public String toString() {
			return "local";
		}
	};
}
