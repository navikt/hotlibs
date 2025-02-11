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
                implementation(libs.jackson.databind)
                implementation(libs.jackson.module.kotlin)
            }
        }
    }
}
