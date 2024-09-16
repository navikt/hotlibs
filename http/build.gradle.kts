plugins {
    id("buildlogic.kotlin-library-conventions")
}

dependencies {
    api(project(":core"))

    // Logging
    implementation(libs.kotlin.logging)
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

    // Jackson
    api(libs.jackson.databind)
    implementation(libs.jackson.datatype.jsr310)
    implementation(libs.jackson.module.kotlin)

    // JWT
    implementation(libs.java.jwt)

    // Cache
    api(libs.caffeine)

    // Testing
    testImplementation(libs.kotest.assertions.ktor)
    testRuntimeOnly(libs.logback.classic)
}
