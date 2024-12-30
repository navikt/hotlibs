plugins {
    id("buildlogic.kotlin-library-conventions")
}

dependencies {
    api(libs.kotlin.logging)
    runtimeOnly(libs.bundles.logging.runtime)
}
