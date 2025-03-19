package no.nav.hjelpemidler.logging

fun logStatement(message: String, vararg pairs: Pair<String, Any?>): String =
    mapOf(*pairs)
        .map {
            val value = when (val rawValue = it.value) {
                is CharSequence -> "'$rawValue'"
                else -> rawValue.toString()
            }
            "${it.key}: $value"
        }
        .joinToString(separator = ", ", prefix = "$message (", postfix = ")")
