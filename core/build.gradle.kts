plugins {
    id("buildlogic.kotlin-library-conventions")

    `java-test-fixtures`
}

dependencies {
    implementation(libs.kotlin.reflect)
    implementation(libs.nocommons)

    // Jackson
    compileOnly(libs.jackson.annotations)
    compileOnly(libs.jackson.databind)

    // kotlinx-serialization
    compileOnly(libs.kotlinx.serialization.core)
    compileOnly(libs.kotlinx.serialization.json)

    // Fixtures
    testFixturesCompileOnly(libs.jackson.annotations)
    testFixturesCompileOnly(libs.kotlinx.serialization.core)
}

@Suppress("UnstableApiUsage")
testing {
    suites {
        withType<JvmTestSuite> {
            dependencies {
                implementation(project())
                implementation(testFixtures(project()))
            }
            targets {
                all {
                    testTask.configure {
                        environment("TEST_CONFIGURATION_ENV_VAR_OVER_PROPERTIES", "9001")
                    }
                }
            }
        }

        val jacksonTest by registering(JvmTestSuite::class) {
            dependencies {
                implementation(project(":serialization"))
            }
        }
        val serializationTest by registering(JvmTestSuite::class) {
            dependencies {
                implementation(libs.kotlinx.serialization.json)
            }
        }
    }
}

@Suppress("UnstableApiUsage")
tasks.named("check") {
    dependsOn(testing.suites.named("jacksonTest"), testing.suites.named("serializationTest"))
}
