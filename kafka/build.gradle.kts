plugins {
    id("buildlogic.kotlin-library-conventions")
}

dependencies {
    api(project(":core"))

    // Kafka
    api(libs.kafka.clients)

    // Kotlinx
    implementation(libs.kotlinx.coroutines.core)
}
