plugins {
    id("buildlogic.kotlin-library-conventions")
}

dependencies {
    // Kafka
    api(project(":kafka"))

    // Rapid and rivers
    api(libs.rapidsAndRivers)
}
