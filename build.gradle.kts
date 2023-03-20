import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

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
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    runtimeOnly(kotlin("reflect"))

    // Database
    api("com.github.seratch:kotliquery:1.9.0")
    api("com.zaxxer:HikariCP:5.0.1")
    api("org.flywaydb:flyway-core:9.15.2")
    implementation("org.postgresql:postgresql:42.5.4")

    // Jackson
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.14.2")

    // Testing
    testImplementation(kotlin("test"))
    val testcontainersVersion = "1.17.6"
    testImplementation("org.testcontainers:postgresql:$testcontainersVersion")
    testImplementation("org.testcontainers:junit-jupiter:$testcontainersVersion")
    testRuntimeOnly("org.slf4j:slf4j-simple:2.0.6")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}
