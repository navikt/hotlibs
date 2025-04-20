plugins {
    id("buildlogic.kotlin-library-conventions")
}

dependencies {
    api(project(":core"))

    // Jackson
    api(libs.bundles.jackson)

    // kotlinx-serialization
    compileOnly(libs.kotlinx.serialization.core)
}
