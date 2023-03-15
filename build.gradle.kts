import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val coroutinesVersion = "1.6.4"
val ktorVersion = "2.2.4"
val jacksonVersion = "2.14.2"
val javaJwtVersion = "3.19.3" // følger ktor-server-auth-jwt
val caffeineVersion = "3.1.4"
val kotlinLoggingVersion = "3.0.5"
val slf4jVersion = "2.0.3"

plugins {
    kotlin("jvm") version "1.8.10"
}

group = "no.nav.hjelpemidler.http"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))

    // Kotlinx
    fun kotlinx(name: String) = "org.jetbrains.kotlinx:kotlinx-$name"
    api(kotlinx("coroutines-core:$coroutinesVersion"))
    implementation(kotlinx("coroutines-jdk8:$coroutinesVersion"))

    // Ktor
    fun ktor(name: String) = "io.ktor:ktor-$name:$ktorVersion"
    implementation(ktor("serialization-jackson"))

    // Ktor Client
    api(ktor("client-core"))
    api(ktor("client-cio"))
    api(ktor("client-mock"))
    implementation(ktor("client-content-negotiation"))

    // Jackson
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jacksonVersion")

    // JWT
    implementation("com.auth0:java-jwt:$javaJwtVersion")

    // Cache
    api("com.github.ben-manes.caffeine:caffeine:$caffeineVersion")

    // Logging
    implementation("io.github.microutils:kotlin-logging:$kotlinLoggingVersion")

    // Testing
    testImplementation(kotlin("test"))
    testImplementation("io.mockk:mockk:1.13.4")
    testRuntimeOnly("org.slf4j:slf4j-simple:$slf4jVersion")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}
