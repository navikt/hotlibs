plugins {
    id("buildlogic.kotlin-library-conventions")
}

dependencies {
    api(project(":kafka"))
    api(project(":logging"))
    api(project(":serialization"))

    // Kafka Streams
    api(libs.kafka.streams)
    api(libs.kafka.streams.avro.serde)

    // Ktor
    api(libs.ktor.server.core)

    implementation(libs.kotlinx.coroutines.core)
}
