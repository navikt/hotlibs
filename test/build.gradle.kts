plugins {
    id("buildlogic.kotlin-library-conventions")
}

dependencies {
    api(libs.kotlin.test.junit5)
    api(libs.kotlinx.coroutines.test)
    api(libs.kotest.assertions.core)
    api(libs.kotest.assertions.json)
    constraints {
        // CVE-2024-57699
        implementation("net.minidev:json-smart:2.6.0")
    }
    api(libs.mockk) {
        exclude("junit", "junit")
    }
    runtimeOnly(libs.logback.classic)
}
