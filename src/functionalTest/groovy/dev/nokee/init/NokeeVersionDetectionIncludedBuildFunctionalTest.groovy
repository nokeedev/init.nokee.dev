package dev.nokee.init

import dev.gradleplugins.test.fixtures.file.TestFile

class NokeeVersionDetectionIncludedBuildFunctionalTest extends AbstractNokeeVersionDetectionFunctionalTest {
	def setup() {
		testDirectory.file('nested', buildFileName).createFile()
		testDirectory.file('nested', settingsFileName).createFile()
		testDirectory.file(settingsFileName) << '''
			includeBuild('nested')
		'''
	}

	@Override
	protected String getShowVersionFlag() {
		return '-Dshow-version'
	}

	@Override
	protected String getBuildPathUnderTest() {
		return ':nested'
	}

	@Override
	TestFile file(Object... path) {
		return super.file('nested', *path)
	}
}
