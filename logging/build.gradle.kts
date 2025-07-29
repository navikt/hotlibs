plugins {
    id("buildlogic.kotlin-library-conventions")
}

dependencies {
    api(libs.kotlin.logging)
    implementation(libs.bundles.logging.runtime)
}
