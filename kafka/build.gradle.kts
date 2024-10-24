plugins {
    id("buildlogic.kotlin-library-conventions")
}

dependencies {
    api(project(":core"))
    api(libs.kafka.clients)

    // Logging
    implementation(libs.kotlin.logging)

    // Kotlinx
    implementation(libs.kotlinx.coroutines.core)

    // Testing
    testRuntimeOnly(libs.slf4j.simple)
}
