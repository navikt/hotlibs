plugins {
    id("buildlogic.kotlin-library-conventions")
}

dependencies {
    api(project(":kafka"))
    api(project(":logging"))
    api(project(":serialization"))

    api(libs.kotlinx.coroutines.core)

    // Kafka Streams
    api(libs.kafka.streams)
    api(libs.kafka.streams.avro.serde)
    constraints {
        implementation(libs.commons.compress)
    }

    // Ktor
    api(libs.ktor.server.core)
    api(libs.ktor.server.netty)
    implementation(libs.ktor.server.metrics.micrometer)

    // Metrics
    api(libs.micrometer.core)
    implementation(libs.micrometer.registry.prometheus)
}
