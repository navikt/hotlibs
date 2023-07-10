import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.kotlin)
}

group = "no.nav.hjelpemidler.database"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(enforcedPlatform(libs.kotlin.bom))
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlin.reflect)
    implementation(libs.kotlinx.coroutines.core)

    // Logging
    implementation(libs.slf4j.api)

    // Database
    api(libs.kotliquery)
    api(libs.hikaricp)
    api(libs.flyway.core)
    implementation(libs.postgresql)

    // Jackson
    implementation(libs.jackson.module.kotlin)

    // Testing
    testImplementation(libs.bundles.test)
    testRuntimeOnly(libs.testcontainers.postgresql)
    testRuntimeOnly(libs.slf4j.simple)
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        showExceptions = true
        showStackTraces = false
        exceptionFormat = TestExceptionFormat.SHORT
        events = setOf(TestLogEvent.PASSED, TestLogEvent.SKIPPED, TestLogEvent.FAILED)
    }
}
