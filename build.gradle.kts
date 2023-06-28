import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.kotlin)
}

group = "no.nav.hjelpemidler.http"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(enforcedPlatform(libs.kotlin.bom))
    implementation(libs.kotlin.stdlib)

    // Kotlinx
    implementation(enforcedPlatform(libs.kotlinx.coroutines.bom))
    api(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.jdk8)
    implementation(libs.kotlinx.coroutines.slf4j)

    // Ktor
    implementation(enforcedPlatform(libs.ktor.bom))
    api(libs.ktor.client.core)
    api(libs.ktor.client.cio)
    api(libs.ktor.client.mock)
    api(libs.ktor.client.core)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.jackson)

    // Jackson
    implementation(libs.jackson.datatype.jsr310)

    // JWT
    implementation(libs.java.jwt)

    // Cache
    api(libs.caffeine)

    // Logging
    implementation(libs.kotlin.logging)
    implementation(libs.ktor.client.logging)

    // Testing
    testImplementation(libs.bundles.test)
    testImplementation(libs.kotest.assertions.ktor)
    testRuntimeOnly(libs.logback.classic)
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}

tasks.withType<Test> {
    useJUnitPlatform()
}
