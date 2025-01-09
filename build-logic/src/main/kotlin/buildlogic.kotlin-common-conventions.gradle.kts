import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")

    id("com.diffplug.spotless")
}

// Gjør det mulig å bruke versjonskatalogen i convention plugins
// se https://github.com/gradle/gradle/issues/15383#issuecomment-779893192
val libs = the<LibrariesForLibs>()

dependencies {
    api(platform(project(":platform")))

    // Logging
    implementation(libs.kotlin.logging)
}

@Suppress("UnstableApiUsage")
testing {
    suites {
        withType<JvmTestSuite> {
            useKotlinTest(libs.versions.kotlin.asProvider())

            dependencies {
                implementation(project(":test"))
            }

            targets.configureEach {
                testTask {
                    environment("NAIS_CLUSTER_NAME", "test")
                    testLogging {
                        events(TestLogEvent.PASSED, TestLogEvent.SKIPPED, TestLogEvent.FAILED)
                    }
                }
            }
        }
    }
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}
