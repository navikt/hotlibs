/**
 * Inspirert av:
 * https://docs.gradle.org/current/userguide/feature_variants.html
 * https://github.com/jjohannes/understanding-gradle/tree/main/17_Feature_Variants
 */

import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    `java-library`
}

// Gjør det mulig å bruke versjonskatalogen i convention plugins
// se https://github.com/gradle/gradle/issues/15383#issuecomment-779893192
val libs = the<LibrariesForLibs>()

group = "no.nav.hjelpemidler"
version = System.getenv("GITHUB_REF_NAME") ?: "local"

val h2 = sourceSets.create("h2")
val oracle = sourceSets.create("oracle")
val postgresql = sourceSets.create("postgresql") // inkluderer flyway
val testcontainers = sourceSets.create("testcontainers")

java {
    val capabilityGroup = project.group.toString()
    val capabilityVersion = project.version.toString()

    registerFeature(h2.name) {
        usingSourceSet(h2)
        capability(capabilityGroup, "${project.name}-${h2.name}", capabilityVersion)
        withSourcesJar()
    }

    registerFeature(oracle.name) {
        usingSourceSet(oracle)
        capability(capabilityGroup, "${project.name}-${oracle.name}", capabilityVersion)
        withSourcesJar()
    }

    registerFeature(postgresql.name) {
        usingSourceSet(postgresql)
        capability(capabilityGroup, "${project.name}-${postgresql.name}", capabilityVersion)
        withSourcesJar()
    }

    registerFeature(testcontainers.name) {
        usingSourceSet(testcontainers)
        capability(capabilityGroup, "${project.name}-${testcontainers.name}", capabilityVersion)
        withSourcesJar()
    }
}

@Suppress("UnstableApiUsage")
testing {
    suites {
        withType<JvmTestSuite> {
            useKotlinTest(libs.versions.kotlin)
            dependencies {
                implementation(libs.kotlinx.coroutines.test)
                implementation(libs.kotest.assertions.core)
                runtimeOnly(libs.slf4j.simple)
            }

            targets.configureEach {
                testTask {
                    testLogging {
                        events(TestLogEvent.PASSED, TestLogEvent.SKIPPED, TestLogEvent.FAILED)
                    }
                }
            }
        }

        val test by getting(JvmTestSuite::class) {}

        val postgresqlTest by registering(JvmTestSuite::class) {
            dependencies {
                implementation(project(path))
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

@Suppress("UnstableApiUsage")
tasks.named("check") {
    dependsOn(testing.suites.named("postgresqlTest"))
}
