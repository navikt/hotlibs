plugins {
    id("buildlogic.kotlin-library-conventions")
}

dependencies {
    api(project(":core"))
    api(libs.kafka.clients)
    implementation(libs.kotlin.reflect)
    implementation(libs.jackson.annotations)
    implementation(libs.kotlinx.coroutines.core)
}
