package no.nav.hjelpemidler.database

import org.flywaydb.core.api.configuration.FluentConfiguration
import org.intellij.lang.annotations.Language

fun FluentConfiguration.initSql(@Language("SQL") vararg initSql: String): FluentConfiguration =
    initSql(initSql.joinToString(";"))
