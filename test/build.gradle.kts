plugins {
    id("buildlogic.kotlin-library-conventions")
}

dependencies {
    api(libs.junit.jupiter.api)
    api(libs.kotlinx.coroutines.test)
    api(libs.mockk)

    // kotest
    api(libs.kotest.assertions.core)
    api(libs.kotest.assertions.json)

    runtimeOnly(libs.logback.classic)
}
