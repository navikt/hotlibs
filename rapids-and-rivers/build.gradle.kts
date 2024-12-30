plugins {
    id("buildlogic.kotlin-library-conventions")
}

dependencies {
    api(project(":kafka"))
    api(project(":serialization"))

    // Rapids and rivers
    api(libs.rapidsAndRivers)
}
