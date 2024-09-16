plugins {
    alias(libs.plugins.kotlin.jvm)
    id("buildlogic.kotlin-library-conventions")
    `maven-publish`
}

dependencies {
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlin.reflect)
    implementation(libs.kotlinx.coroutines.core)
    api(libs.hm.core)
    compileOnly(libs.jetbrains.annotations)

    // Logging
    implementation(libs.slf4j.api)

    // Jackson
    implementation(libs.bundles.jackson)

    // JDBC
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
}

java {
    toolchain { languageVersion.set(JavaLanguageVersion.of(21)) }
    withSourcesJar()
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
