package no.nav.hjelpemidler.text

import java.math.BigDecimal
import java.math.BigInteger
import java.net.URI
import java.net.URL
import java.util.UUID

private val intRange: CharRange = '0'..'9'

/**
 * @return `true` hvis teksten kun består av heltall.
 */
fun CharSequence.isInteger(): Boolean =
    isNotEmpty() && all { it in intRange }

/**
 * Konverter til [java.util.UUID].
 */
fun String.toUUID(): UUID =
    UUID.fromString(this)

/**
 * Konverter til [java.net.URI].
 */
fun String.toURI(): URI =
    URI(this)

/**
 * Konverter til [java.net.URL].
 */
fun String.toURL(): URL =
    toURI().toURL()

inline fun <reified T : Any> String.parseTo(): T =
    when (val type = T::class) {
        BigDecimal::class -> toBigDecimal()
        BigInteger::class -> toBigInteger()
        Boolean::class -> toBoolean()
        Double::class -> toDouble()
        Float::class -> toFloat()
        Int::class -> toInt()
        Long::class -> toLong()
        String::class -> this
        URI::class -> toURI()
        URL::class -> toURL()
        UUID::class -> toUUID()
        else -> error("Ukjent type: '$type'")
    } as T

fun String.isWrappedWith(prefix: String, suffix: String = prefix, ignoreCase: Boolean = false): Boolean =
    startsWith(prefix, ignoreCase) && endsWith(suffix, ignoreCase)

fun String.isWrappedWith(startChar: Char, endChar: Char = startChar, ignoreCase: Boolean = false): Boolean =
    startsWith(startChar, ignoreCase) && endsWith(endChar, ignoreCase)

fun String.quoted(): String = when {
    isEmpty() -> "''"
    isWrappedWith("'") -> this
    else -> "'$this'"
}

fun String.doubleQuoted(): String = when {
    isEmpty() -> "\"\""
    isWrappedWith('"') -> this
    else -> "\"$this\""
}

fun String.unquoted(): String = removeSurrounding("'").removeSurrounding("\"")
