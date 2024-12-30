plugins {
    id("buildlogic.kotlin-database-conventions")
}

dependencies {
    api(project(":core"))
    api(project(":serialization"))

    implementation(libs.kotlin.reflect)
    implementation(libs.kotlinx.coroutines.core)
    compileOnly(libs.jetbrains.annotations)

    // JDBC
    api(libs.kotliquery)
    api(libs.hikaricp)

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

    // Testcontainers
    testcontainersApi(project(path))
    testcontainersRuntimeOnly(libs.testcontainers.postgresql) // fixme -> kunne vi valgt oracle hvis oracle-capability?
}


@Suppress("UnstableApiUsage")
tasks.named("check") {
    dependsOn(testing.suites.named("postgresqlTest"))
}
