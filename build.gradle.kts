import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val coroutinesVersion = "1.7.1"
val ktorVersion = "2.3.0"
val jacksonVersion = "2.15.0"
val javaJwtVersion = "4.4.0" // følger ktor-server-auth-jwt
val caffeineVersion = "3.1.6"
val kotlinLoggingVersion = "3.0.5"
val logbackVersion = "1.4.7"
val mockkVersion = "1.13.5"
val kotestVersion = "5.6.2"
val kotestAssertionsKtorVersion = "2.0.0"

plugins {
    kotlin("jvm") version "1.8.20"
}

group = "no.nav.hjelpemidler.http"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))

    // Kotlinx
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:$coroutinesVersion")

    // Ktor
    api("io.ktor:ktor-client-core:$ktorVersion")
    api("io.ktor:ktor-client-cio:$ktorVersion")
    api("io.ktor:ktor-client-mock:$ktorVersion")
    implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-serialization-jackson:$ktorVersion")

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
    testImplementation("io.mockk:mockk:$mockkVersion")
    testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
    testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
    testImplementation("io.kotest.extensions:kotest-assertions-ktor:$kotestAssertionsKtorVersion")
    testRuntimeOnly("ch.qos.logback:logback-classic:$logbackVersion")
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
