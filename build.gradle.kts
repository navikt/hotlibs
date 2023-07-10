import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.kotlin)
    `java-library`
    `maven-publish`
}

group = "no.nav.hjelpemidler"
version = System.getenv("GITHUB_REF_NAME") ?: "local"

repositories {
    mavenCentral()
}

dependencies {
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

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17

    withSourcesJar()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/navikt/hm-database")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
}
