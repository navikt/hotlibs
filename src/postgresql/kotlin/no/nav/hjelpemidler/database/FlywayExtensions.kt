package no.nav.hjelpemidler.database

import org.flywaydb.core.api.configuration.FluentConfiguration

fun FluentConfiguration.initSql(vararg initSql: String): FluentConfiguration =
    initSql(initSql.joinToString(";"))
