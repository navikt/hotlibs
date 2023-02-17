import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val ktorVersion = "2.2.3"
val jacksonVersion = "2.14.2"

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

    // Ktor
    fun ktor(name: String) = "io.ktor:ktor-$name:$ktorVersion"
    implementation(ktor("serialization-jackson"))

    // Ktor Client
    api(ktor("client-core"))
    api(ktor("client-cio"))
    api(ktor("client-mock"))
    implementation(ktor("client-auth"))
    implementation(ktor("client-content-negotiation"))

    // Jackson
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jacksonVersion")

    // Testing
    testImplementation(kotlin("test"))
    testImplementation("io.mockk:mockk:1.13.4")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}
