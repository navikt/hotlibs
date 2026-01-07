package no.nav.hjelpemidler.database.jdbc

import java.sql.ResultSet
import kotlin.reflect.KClass

fun <T : Any> ResultSet.getObject(columnIndex: Int, type: KClass<T>): T? =
    getObject(columnIndex, type.java).takeUnless { wasNull() }
