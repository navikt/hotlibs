plugins {
    id("buildlogic.kotlin-library-conventions")
}

dependencies {
    api(libs.kotlinx.coroutines.test)
    api(libs.kotest.assertions.core)
    api(libs.mockk)
    runtimeOnly(libs.logback.classic)
}