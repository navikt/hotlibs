plugins {
    id("buildlogic.kotlin-database-conventions")

    alias(libs.plugins.kotlin.jpa)
}

dependencies {
    api(project(":core"))
    api(project(":serialization"))

    implementation(libs.kotlin.reflect)
    implementation(libs.kotlinx.coroutines.core)
    compileOnly(libs.jetbrains.annotations)

    // JDBC
    api(libs.hikaricp)
    implementation(libs.kotliquery)

    // H2
    h2Api(project(path))
    h2RuntimeOnly(libs.h2)

    // Ktor
    ktorApi(project(path))
    ktorApi(libs.ktor.server.core)

    // Oracle
    oracleApi(project(path))
    oracleRuntimeOnly(libs.ojdbc11)

    // PostgreSQL
    postgresqlApi(project(path))
    postgresqlImplementation(libs.postgresql)

    // PostgreSQL - Flyway
    postgresqlApi(libs.flyway.core)
    postgresqlRuntimeOnly(libs.flyway.database.postgresql)

    // Repository
    repositoryApi(project(path))
    repositoryApi(libs.hibernate.core)
    repositoryImplementation(libs.kotlinx.coroutines.core)
    repositoryRuntimeOnly(libs.slf4j.jdk.platform.logging)

    // Testcontainers
    testcontainersApi(project(path))
    testcontainersRuntimeOnly(libs.testcontainers.postgresql) { // fixme -> kunne vi valgt oracle hvis oracle-capability?
        exclude("junit", "junit")
    }
}


@Suppress("UnstableApiUsage")
tasks.named("check") {
    dependsOn(
        testing.suites.named("postgresqlTest"),
        testing.suites.named("repositoryTest"),
    )
}
