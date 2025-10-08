/**
 * Inspirert av:
 * https://docs.gradle.org/current/userguide/feature_variants.html
 * https://github.com/jjohannes/understanding-gradle/tree/main/17_Feature_Variants
 */

import org.gradle.accessors.dm.LibrariesForLibs

plugins {
    id("buildlogic.kotlin-library-conventions")
}

// Gjør det mulig å bruke versjonskatalogen i convention plugins
// se https://github.com/gradle/gradle/issues/15383#issuecomment-779893192
val libs = the<LibrariesForLibs>()

val h2: SourceSet = sourceSets.create("h2")
val ktor: SourceSet = sourceSets.create("ktor")
val oracle: SourceSet = sourceSets.create("oracle")
val postgresql: SourceSet = sourceSets.create("postgresql") // inkluderer flyway
val testcontainers: SourceSet = sourceSets.create("testcontainers")

java {
    registerFeature(h2.name) {
        usingSourceSet(h2)
        withSourcesJar()
    }

    registerFeature(ktor.name) {
        usingSourceSet(ktor)
        withSourcesJar()
    }

    registerFeature(oracle.name) {
        usingSourceSet(oracle)
        withSourcesJar()
    }

    registerFeature(postgresql.name) {
        usingSourceSet(postgresql)
        withSourcesJar()
    }

    registerFeature(testcontainers.name) {
        usingSourceSet(testcontainers)
        withSourcesJar()
    }
}

@Suppress("UnstableApiUsage")
testing {
    suites {
        val test by getting(JvmTestSuite::class)
        val postgresqlTest by registering(JvmTestSuite::class) {
            dependencies {
                implementation(project(path)) {
                    capabilities {
                        requireCapability("${project.group}:${project.name}-${postgresql.name}")
                    }
                }
                implementation(project(path)) {
                    capabilities {
                        requireCapability("${project.group}:${project.name}-${testcontainers.name}")
                    }
                }
            }

            targets {
                all {
                    testTask.configure {
                        shouldRunAfter(test)
                    }
                }
            }
        }
    }
}
