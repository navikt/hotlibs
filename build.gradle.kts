import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.kotlin)
    `maven-publish`
}

group = "no.nav.hjelpemidler"
version = System.getenv("GITHUB_REF_NAME") ?: "local"

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.kotlin.stdlib)

    // Kotlinx
    api(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.jdk8)
    implementation(libs.kotlinx.coroutines.slf4j)

    // Ktor
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
