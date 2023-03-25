import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val coroutinesVersion = "1.6.4"
val slf4jVersion = "2.0.7"
val kotestVersion = "5.5.5"
val testcontainersVersion = "1.17.6"

plugins {
    kotlin("jvm") version "1.8.10"
}

group = "no.nav.hjelpemidler.database"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    runtimeOnly(kotlin("reflect"))

    // Logging
    implementation("org.slf4j:slf4j-api:$slf4jVersion")

    // Database
    api("com.github.seratch:kotliquery:1.9.0")
    api("com.zaxxer:HikariCP:5.0.1")
    api("org.flywaydb:flyway-core:9.16.1")
    implementation("org.postgresql:postgresql:42.6.0")

    // Jackson
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.14.2")

    // Testing
    testImplementation(kotlin("test"))
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion")
    testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
    testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
    testRuntimeOnly("org.testcontainers:postgresql:$testcontainersVersion")
    testRuntimeOnly("org.slf4j:slf4j-simple:$slf4jVersion")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}

kotlin {
    sourceSets {
        test {
            languageSettings.optIn("kotlinx.coroutines.ExperimentalCoroutinesApi")
        }
    }
}
