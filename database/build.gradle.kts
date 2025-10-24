plugins {
    id("buildlogic.kotlin-database-conventions")

    alias(libs.plugins.kotlin.jpa)

    `java-test-fixtures`
}

dependencies {
    api(project(":core"))
    api(project(":serialization"))

    implementation(libs.kotlin.reflect)
    implementation(libs.kotlinx.coroutines.core)
    compileOnly(libs.jetbrains.annotations)

    // JDBC
    api(libs.hikaricp)
    // Ikke api() siden vi ikke eksponerer kotliquery ut
    implementation(libs.kotliquery)

    // H2
    h2Api(project(path))
    h2RuntimeOnly(libs.h2)

    // Ktor
    ktorApi(project(path))
    ktorApi(libs.ktor.server.core)

    // Oracle
    oracleApi(project(path))
    oracleImplementation(libs.ojdbc11)

    // PostgreSQL
    postgresqlApi(project(path))
    postgresqlImplementation(libs.postgresql)

    // PostgreSQL - Flyway
    postgresqlApi(libs.flyway.core)
    postgresqlRuntimeOnly(libs.flyway.database.postgresql)

    // Testcontainers
    testcontainersApi(project(path))
    testcontainersRuntimeOnly(libs.testcontainers.postgresql) // fixme -> kunne vi valgt oracle hvis oracle-capability?
}


@Suppress("UnstableApiUsage")
tasks.named("check") {
    dependsOn(
        testing.suites.named("oracleTest"),
        testing.suites.named("postgresqlTest"),
    )
}
