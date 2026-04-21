package no.nav.hjelpemidler.database

import java.sql.ResultSet
import kotlin.reflect.KClass

internal fun <T : Any> ResultSet.getObject(columnIndex: Int, type: KClass<T>): T? =
    getObject(columnIndex, type.java).takeUnless { wasNull() }

internal fun <T : Any> ResultSet.getObject(columnLabel: String, type: KClass<T>): T? =
    getObject(columnLabel, type.java).takeUnless { wasNull() }
