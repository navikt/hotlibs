plugins {
    id("buildlogic.kotlin-database-conventions")
}

dependencies {
    api(project(":core"))
    api(project(":serialization"))

    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlin.reflect)
    implementation(libs.kotlinx.coroutines.core)
    compileOnly(libs.jetbrains.annotations)

    // Logging
    implementation(libs.slf4j.api)

    // JDBC
    api(libs.kotliquery)
    api(libs.hikaricp)

    // H2
    h2Api(project(path))
    h2RuntimeOnly(libs.h2)

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
    constraints {
        testRuntimeOnly(libs.commons.compress)
    }
}


@Suppress("UnstableApiUsage")
tasks.named("check") {
    dependsOn(testing.suites.named("postgresqlTest"))
}
