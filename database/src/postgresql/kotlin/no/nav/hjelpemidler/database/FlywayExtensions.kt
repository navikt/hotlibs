package no.nav.hjelpemidler.database

import org.flywaydb.core.api.configuration.FluentConfiguration
import org.intellij.lang.annotations.Language

fun FluentConfiguration.initSql(@Language("SQL") vararg initSql: String): FluentConfiguration =
    initSql(initSql.joinToString(";"))


fun FluentConfiguration.createRole(role: String) {
    initSql(
        initSql ?: "",
        """
            DO
            ${'$'}do${'$'}
                BEGIN
                    IF EXISTS (SELECT
                               FROM pg_catalog.pg_roles
                               WHERE rolname = '$role') THEN
                        RAISE NOTICE '"$role" finnes allerede.';
                    ELSE
                        CREATE ROLE $role;
                    END IF;
                END
            ${'$'}do${'$'};
        """.trimIndent(),
    )
}
