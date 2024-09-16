package no.nav.hjelpemidler.domain.id

import java.util.UUID

/**
 * Lag tilfeldig [UUID].
 */
fun UUID(): UUID = UUID.randomUUID()

/**
 * Lag [UUID] fra [value].
 */
fun UUID(value: String): UUID = UUID.fromString(value)

/**
 * Konverter til [UUID].
 */
fun String.toUUID(): UUID = UUID(this)
