import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
    `java-library`
    `maven-publish`
}

group = "no.nav.hjelpemidler"
version = System.getenv("GITHUB_REF_NAME") ?: "local"

dependencies {
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlin.reflect)
    implementation(libs.kotlin.logging)
    implementation(libs.nocommons)

    // Jackson
    compileOnly(libs.jackson.annotations)

    // kotlinx.serialization
    compileOnly(libs.kotlinx.serialization.core)
    compileOnly(libs.kotlinx.serialization.json)

    // Testing
    testImplementation(libs.bundles.test)
    testImplementation(libs.bundles.jackson)
    testImplementation(libs.kotlinx.serialization.json)
    testRuntimeOnly(libs.slf4j.simple)
}

val jdkVersion = JavaLanguageVersion.of(21)
java {
    toolchain { languageVersion.set(jdkVersion) }
    withSourcesJar()
}
kotlin { jvmToolchain { languageVersion.set(jdkVersion) } }

tasks.test {
    useJUnitPlatform()
    testLogging {
        events(TestLogEvent.PASSED, TestLogEvent.SKIPPED, TestLogEvent.FAILED)
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
    repositories {
        maven {
            url = uri("https://maven.pkg.github.com/navikt/hm-core")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
}
