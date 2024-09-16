plugins {
    id("buildlogic.kotlin-library-conventions")
}

dependencies {
    implementation(libs.kotlin.reflect)
    implementation(libs.kotlin.logging)
    implementation(libs.nocommons)

    // Jackson
    compileOnly(libs.jackson.annotations)

    // kotlinx.serialization
    compileOnly(libs.kotlinx.serialization.core)
    compileOnly(libs.kotlinx.serialization.json)

    // Testing
    testImplementation(libs.bundles.jackson)
    testImplementation(libs.kotlinx.serialization.json)
    testRuntimeOnly(libs.slf4j.simple)
}
