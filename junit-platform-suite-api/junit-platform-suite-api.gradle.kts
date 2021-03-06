plugins {
	`java-library-conventions`
}

description = "JUnit Platform Suite API"

dependencies {
	api(platform(project(":junit-bom")))

	api("org.apiguardian:apiguardian-api:${Versions.apiGuardian}")

	testRuntimeOnly(project(":junit-platform-commons"))
}
