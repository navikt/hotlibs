plugins {
    id("buildlogic.kotlin-library-conventions")
}

dependencies {
    api(project(":core"))
    api(libs.bundles.jackson)
    implementation(libs.kotlin.logging)

    // Testing
    testRuntimeOnly(libs.slf4j.simple)
}
