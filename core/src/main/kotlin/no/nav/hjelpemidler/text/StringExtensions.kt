package no.nav.hjelpemidler.text

import java.util.UUID

private val intRange: CharRange = '0'..'9'

/**
 * @return `true` hvis teksten kun består av heltall.
 */
fun CharSequence.isInteger(): Boolean =
    all { it in intRange }

/**
 * Konverter til [UUID].
 */
fun String.toUUID(): UUID = UUID.fromString(this)
