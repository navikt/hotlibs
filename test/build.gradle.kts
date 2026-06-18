plugins {
    id("buildlogic.kotlin-library-conventions")
}

val ktor: SourceSet = sourceSets.create("ktor")

java {
    registerFeature(ktor.name) {
        usingSourceSet(ktor)
        withSourcesJar()
    }
}

dependencies {
    api(libs.junit.jupiter.api)
    api(libs.kotlinx.coroutines.test)
    api(libs.mockk)

    // kotest
    api(libs.kotest.assertions.core)
    api(libs.kotest.assertions.json)

    runtimeOnly(libs.logback.classic)

    // ktor
    "ktorApi"(libs.ktor.server.test.host)
    "ktorImplementation"(libs.ktor.serialization.jackson3)
    "ktorImplementation"(libs.ktor.server.content.negotiation)
    "ktorImplementation"(libs.ktor.server.status.pages)
    "ktorImplementation"(project(":serialization"))
}

@Suppress("UnstableApiUsage")
testing {
    suites {
        val ktorTest by registering(JvmTestSuite::class) {
            useKotlinTest(libs.versions.kotlin.asProvider())
            val currentProject = project
            dependencies {
                implementation(project())
                implementation(project()) {
                    capabilities {
                        requireCapability("${currentProject.group}:${currentProject.name}-${ktor.name}")
                    }
                }
                implementation(project(":http"))
            }
        }
    }
}

@Suppress("UnstableApiUsage")
tasks.named("check") {
    dependsOn(testing.suites.named("ktorTest"))
}
