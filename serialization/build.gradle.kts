plugins {
    id("buildlogic.kotlin-library-conventions")
}

dependencies {
    api(project(":core"))
    implementation(libs.bundles.jackson)
    implementation(libs.kotlin.logging)

    // Testing
    testRuntimeOnly(libs.slf4j.simple)
}
