plugins {
    id("buildlogic.kotlin-library-conventions")

    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    api(project(":core"))
    api(project(":http"))

    implementation(libs.ktor.client.resources)

    testImplementation(project(":test")) {
        capabilities {
            requireCapability("no.nav.hjelpemidler:test-ktor")
        }
    }
}
