plugins {
    id("buildlogic.kotlin-library-conventions")
}

dependencies {
    // Jackson
    implementation(libs.jackson.annotations)
    compileOnly(libs.jackson.databind)
}

@Suppress("UnstableApiUsage")
testing {
    suites {
        withType<JvmTestSuite> {
            dependencies {
                implementation(project(":serialization"))
            }
        }
    }
}
