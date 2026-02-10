plugins {
    id("buildlogic.kotlin-library-conventions")
}

dependencies {
    api(project(":kafka"))
    api(project(":logging"))
    api(project(":serialization"))
    api(libs.rapidsAndRivers) {
        // rapids-and-rivers har endret til 9.0 som tar inn Jackson 3.0
        exclude(module = "logstash-logback-encoder")
    }

    testImplementation(libs.tbdLibs.rapidsAndRivers.test)
}
