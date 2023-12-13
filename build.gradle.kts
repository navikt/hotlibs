plugins {
    alias(libs.plugins.kotlin.jvm)
    `java-library`
    `maven-publish`
}

group = "no.nav.hjelpemidler"
version = System.getenv("GITHUB_REF_NAME") ?: "local"

dependencies {
    implementation(platform(libs.kotlin.bom))
    implementation(libs.kotlin.stdlib)

    // Logging
    implementation(platform(libs.slf4j.bom))
    implementation(libs.kotlin.logging)

    // Kotlinx
    api(platform(libs.kotlinx.coroutines.bom))
    api(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.jdk8)
    implementation(libs.kotlinx.coroutines.slf4j)

    // Ktor
    api(platform(libs.ktor.bom))
    api(libs.ktor.client.cio)
    api(libs.ktor.client.core)
    api(libs.ktor.client.core)
    api(libs.ktor.client.logging)
    api(libs.ktor.client.mock)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.jackson)

    // Jackson
    implementation(platform(libs.jackson.bom))
    implementation(libs.jackson.datatype.jsr310)

    // JWT
    implementation(libs.java.jwt)

    // Cache
    api(libs.caffeine)

    // Testing
    testImplementation(libs.bundles.test)
    testImplementation(libs.kotest.assertions.ktor)
    testRuntimeOnly(libs.logback.classic)
}

val jdkVersion = JavaLanguageVersion.of(21)
java {
    toolchain { languageVersion.set(jdkVersion) }
    withSourcesJar()
}
kotlin { jvmToolchain { languageVersion.set(jdkVersion) } }

tasks.test {
    environment("NAIS_CLUSTER_NAME", "local")
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
            url = uri("https://maven.pkg.github.com/navikt/hm-http")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
}
