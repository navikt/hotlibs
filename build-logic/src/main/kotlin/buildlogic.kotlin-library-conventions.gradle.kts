/**
 * Inspirert av:
 * https://docs.gradle.org/current/userguide/feature_variants.html
 * https://github.com/jjohannes/understanding-gradle/tree/main/17_Feature_Variants
 */

plugins {
    `java-library`
}

group = "no.nav.hjelpemidler"
version = System.getenv("GITHUB_REF_NAME") ?: "local"

val h2 = sourceSets.create("h2")
val oracle = sourceSets.create("oracle")
val postgresql = sourceSets.create("postgresql") // inkluderer flyway
val testcontainers = sourceSets.create("testcontainers")

java {
    val capabilityGroup = project.group.toString()
    val capabilityName = project.name
    val capabilityVersion = project.version.toString()

    registerFeature(h2.name) {
        usingSourceSet(h2)
        capability(capabilityGroup, "$capabilityName-h2", capabilityVersion)
        withSourcesJar()
    }

    registerFeature(oracle.name) {
        usingSourceSet(oracle)
        capability(capabilityGroup, "$capabilityName-oracle", capabilityVersion)
        withSourcesJar()
    }

    registerFeature(postgresql.name) {
        usingSourceSet(postgresql)
        capability(capabilityGroup, "$capabilityName-postgresql", capabilityVersion)
        withSourcesJar()
    }

    registerFeature(testcontainers.name) {
        usingSourceSet(testcontainers)
        capability(capabilityGroup, "$capabilityName-testcontainers", capabilityVersion)
        withSourcesJar()
    }
}
