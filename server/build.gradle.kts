plugins {
    id("buildlogic.kotlin-library-conventions")
}

dependencies {
    api(project(":core"))
    api(libs.ktor.server.core)
    api(libs.ktor.server.auth.jwt)

    testImplementation(libs.nimbus.jose.jwt)
}
