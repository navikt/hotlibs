plugins {
    id("buildlogic.kotlin-library-conventions")
}

dependencies {
    api(project(":kafka"))
    api(project(":serialization"))
    api(libs.rapidsAndRivers)
}
