package no.nav.hjelpemidler.database

import org.flywaydb.core.api.configuration.FluentConfiguration
import org.intellij.lang.annotations.Language

fun FluentConfiguration.initSql(@Language("SQL") vararg initSql: String): FluentConfiguration =
    initSql(initSql.filterNot(String::isBlank).joinToString(";"))


fun FluentConfiguration.createRole(role: String) {
    initSql(
        initSql ?: "",
        """
            DO
            ${'$'}${'$'}
                BEGIN
                    CREATE ROLE $role;
                EXCEPTION
                    WHEN duplicate_object THEN RAISE NOTICE '%, skipping', sqlerrm USING ERRCODE = sqlstate;
                END
            ${'$'}${'$'};
        """.trimIndent(),
    )
}
