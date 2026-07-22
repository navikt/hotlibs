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
val oracle: SourceSet = sourceSets.create("oracle")
val postgresql: SourceSet = sourceSets.create("postgresql") // inkluderer flyway
val testcontainers: SourceSet = sourceSets.create("testcontainers")

java {
    registerFeature(h2)
    registerFeature(oracle)
    registerFeature(postgresql)
    registerFeature(testcontainers)
}

fun JavaPluginExtension.registerFeature(sourceSet: SourceSet) = registerFeature(sourceSet.name) {
    usingSourceSet(sourceSet)
    withSourcesJar()
}

@Suppress("UnstableApiUsage")
testing {
    val test = suites.named<JvmTestSuite>("test")
    val h2Test = featureTest("h2Test", h2)
    val oracleTest = featureTest("oracleTest", oracle)
    val postgresqlTest = featureTest("postgresqlTest", postgresql, testcontainers)
}

@Suppress("UnstableApiUsage")
fun TestingExtension.featureTest(name: String, vararg sourceSets: SourceSet) = suites.register<JvmTestSuite>(name) {
    useKotlinTest(libs.versions.kotlin.asProvider())
    val currentProject = project
    dependencies {
        implementation(project())
        sourceSets.forEach { sourceSet ->
            implementation(project()) {
                capabilities {
                    requireCapability("${currentProject.group}:${currentProject.name}-${sourceSet.name}")
                }
            }
        }
        implementation(testFixtures(project()))
    }
}
