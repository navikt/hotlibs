plugins {
    id("buildlogic.kotlin-library-conventions")
}

dependencies {
    // Jackson
    implementation(libs.jackson.annotations)
    compileOnly(libs.jackson.databind)

    // Testing
    testImplementation(project(":serialization"))
}
