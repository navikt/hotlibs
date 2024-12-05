package no.nav.hjelpemidler.text

import java.util.UUID

/**
 * Konverter til [UUID].
 */
fun String.toUUID(): UUID = UUID.fromString(this)
