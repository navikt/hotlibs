plugins {
    id("buildlogic.kotlin-library-conventions")
}

dependencies {
    api(project(":core"))
    api(project(":serialization"))

    // Kotlinx
    api(libs.kotlinx.coroutines.core)

    // Ktor
    api(libs.ktor.client.cio)
    api(libs.ktor.client.core)
    api(libs.ktor.client.logging)
    api(libs.ktor.client.mock)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.jackson3)

    // Cache
    api(libs.caffeine)
    runtimeOnly(libs.slf4j.jdk.platform.logging) // Caffeine uses JDK Platform Logging

    // Testing
    testImplementation(testFixtures(project(":core")))
    testImplementation(libs.java.jwt)
}
