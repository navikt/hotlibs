plugins {
    id("buildlogic.kotlin-library-conventions")
}

dependencies {
    // Kafka
    api(project(":kafka"))
    api(project(":serialization"))

    // Rapid and rivers
    api(libs.rapidsAndRivers)
}
