plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.spotless)
    `java-library`
    `maven-publish`
}

group = "no.nav.hjelpemidler"
version = System.getenv("GITHUB_REF_NAME") ?: "local"

dependencies {
    implementation(libs.kotlin.stdlib)
    implementation(libs.jackson.annotations)

    testImplementation(libs.kotlin.test.junit5)
    testImplementation(libs.kotest.assertions.core)
    testImplementation(libs.jackson.databind)
    testImplementation(libs.jackson.module.kotlin)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17

    withSourcesJar()
}

kotlin {
    jvmToolchain(17)
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
            url = uri("https://maven.pkg.github.com/navikt/hm-nare")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
}
