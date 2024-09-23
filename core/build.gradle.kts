plugins {
    id("buildlogic.kotlin-library-conventions")

    `java-test-fixtures`
}

dependencies {
    implementation(libs.kotlin.reflect)
    implementation(libs.kotlin.logging)
    implementation(libs.nocommons)

    // Jackson
    compileOnly(libs.jackson.annotations)

    // kotlinx.serialization
    compileOnly(libs.kotlinx.serialization.core)

    // Fixtures
    testFixturesCompileOnly(libs.jackson.annotations)
    testFixturesCompileOnly(libs.kotlinx.serialization.core)

    // Testing
    testRuntimeOnly(libs.slf4j.simple)
}

@Suppress("UnstableApiUsage")
testing {
    suites {
        withType<JvmTestSuite> {
            dependencies {
                implementation(project())
                implementation(testFixtures(project()))
                runtimeOnly(libs.slf4j.simple)
            }
        }

        val jacksonTest by registering(JvmTestSuite::class) {
            dependencies {
                implementation(libs.jackson.databind)
                implementation(libs.jackson.module.kotlin)
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
