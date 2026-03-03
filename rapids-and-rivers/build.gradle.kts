plugins {
    id("buildlogic.kotlin-library-conventions")
}

dependencies {
    api(project(":kafka"))
    api(project(":logging"))
    api(project(":serialization"))
    api(libs.rapidsAndRivers)

    testImplementation(libs.tbdLibs.rapidsAndRivers.test)
}
