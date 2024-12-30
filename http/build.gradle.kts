plugins {
    id("buildlogic.kotlin-library-conventions")
}

dependencies {
    api(project(":core"))
    api(project(":serialization"))

    // Logging
    runtimeOnly(libs.slf4j.jdk.platform.logging)

    // Kotlinx
    api(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.jdk8)
    implementation(libs.kotlinx.coroutines.slf4j)

    // Ktor
    api(libs.ktor.client.cio)
    api(libs.ktor.client.core)
    api(libs.ktor.client.logging)
    api(libs.ktor.client.mock)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.jackson)

    // JWT
    implementation(libs.java.jwt)

    // Cache
    api(libs.caffeine)
}
