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

    // Kotest extensions for withEnvironment("KEY", "VAL") { (...) }
    testImplementation(libs.kotest.extensions.jvm)
}

@Suppress("UnstableApiUsage")
testing {
    suites {
        withType<JvmTestSuite> {
            dependencies {
                implementation(project())
                implementation(testFixtures(project()))
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

// Required for kotest extensions for withEnvironment("KEY", "VAL") { (...) }
tasks.withType<Test>().configureEach {
    jvmArgs("--add-opens=java.base/java.util=ALL-UNNAMED")
}
