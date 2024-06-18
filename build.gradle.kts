import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    alias(libs.plugins.kotlin.jvm)
    id("buildlogic.kotlin-library-conventions")
    `maven-publish`
}

dependencies {
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlin.reflect)
    implementation(libs.kotlinx.coroutines.core)

    compileOnly(libs.java.annotations)

    // Logging
    implementation(libs.slf4j.api)

    // Jackson
    implementation(libs.jackson.databind)
    implementation(libs.jackson.datatype.jsr310)
    implementation(libs.jackson.module.kotlin)

    // Database
    api(libs.kotliquery)
    api(libs.hikaricp)

    // H2
    h2Implementation(project(path))
    h2RuntimeOnly(libs.h2)

    // Oracle
    oracleImplementation(project(path))
    oracleRuntimeOnly(libs.ojdbc11)

    // PostgreSQL
    postgresqlImplementation(project(path))
    postgresqlImplementation(libs.postgresql)

    // PostgreSQL - Flyway
    postgresqlApi(libs.flyway.core)
    postgresqlRuntimeOnly(libs.flyway.database.postgresql)

    // Testcontainers
    testcontainersImplementation(project(path))
    testcontainersRuntimeOnly(libs.testcontainers.postgresql) // fixme -> kunne vi valgt oracle hvis oracle-capability?
    constraints {
        testRuntimeOnly(libs.commons.compress)
    }

    // Testing
    testImplementation(project(path)) {
        capabilities {
            requireCapability("${project.group}:${project.name}-postgresql")
        }
    }
    testImplementation(project(path)) {
        capabilities {
            requireCapability("${project.group}:${project.name}-testcontainers")
        }
    }
    testImplementation(libs.bundles.test)
    testRuntimeOnly(libs.slf4j.simple)
}

val jdkVersion = JavaLanguageVersion.of(21)
java {
    toolchain { languageVersion.set(jdkVersion) }
    withSourcesJar()
}
kotlin {
    jvmToolchain { languageVersion.set(jdkVersion) }
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events(TestLogEvent.PASSED, TestLogEvent.SKIPPED, TestLogEvent.FAILED)
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
    repositories {
        maven {
            url = uri("https://maven.pkg.github.com/navikt/hm-database")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
}
