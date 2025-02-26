plugins {
    id("buildlogic.kotlin-library-conventions")
}

dependencies {
    api(libs.kotlinx.coroutines.test)
    api(libs.kotest.assertions.core)
    api(libs.kotest.assertions.json)
    constraints {
        // CVE-2024-57699
        implementation("net.minidev:json-smart:2.5.2")
    }
    api(libs.mockk) {
        exclude("junit", "junit")
    }
    runtimeOnly(libs.logback.classic)
}
