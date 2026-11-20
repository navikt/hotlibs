plugins {
    id("buildlogic.kotlin-library-conventions")

    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    api(project(":core"))
    api(project(":http"))
    api(project(":logging"))
    api(project(":norg"))

    implementation(libs.ktor.client.resources)

    testImplementation(project(":test")) {
        capabilities {
            requireCapability("no.nav.hjelpemidler:test-ktor")
        }
    }
    testImplementation(libs.ktor.server.resources)
}
