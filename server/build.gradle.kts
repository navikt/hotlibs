plugins {
    id("buildlogic.kotlin-library-conventions")

    `java-test-fixtures`
}

dependencies {
    api(project(":core"))
    api(libs.ktor.server.core)
    api(libs.ktor.server.auth.jwt)

    testImplementation(project(":test")) {
        capabilities {
            requireCapability("no.nav.hjelpemidler:test-ktor")
        }
    }

    testFixturesApi(testFixtures(project(":core")))
    testFixturesImplementation(libs.nimbus.jose.jwt)
}
