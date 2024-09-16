plugins {
    id("buildlogic.kotlin-library-conventions")
}

dependencies {
    implementation(libs.jackson.annotations)

    testImplementation(libs.bundles.jackson)
}
