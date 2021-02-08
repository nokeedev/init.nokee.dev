package dev.nokee.init

class NokeeVersionDetectionHostBuildFunctionalTest extends AbstractNokeeVersionDetectionFunctionalTest {
	@Override
	protected String getShowVersionFlag() {
		return '--show-version'
	}

	@Override
	protected String getBuildPathUnderTest() {
		return ':'
	}

}
