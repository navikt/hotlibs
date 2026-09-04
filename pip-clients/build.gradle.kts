plugins {
    id("buildlogic.kotlin-library-conventions")
}

dependencies {
    api(project(":core"))
    api(project(":http"))

    testImplementation(project(":test")) {
        capabilities {
            requireCapability("no.nav.hjelpemidler:test-ktor")
        }
    }
}
