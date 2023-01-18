import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.8.0"
}

group = "no.nav.hjelpemidler.database"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    runtimeOnly(kotlin("reflect"))

    // Database
    api("com.github.seratch:kotliquery:1.9.0")
    implementation("com.zaxxer:HikariCP:5.0.1")
    implementation("org.postgresql:postgresql:42.5.1")
    implementation("org.flywaydb:flyway-core:9.11.0")

    // Jackson
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.14.1")

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
