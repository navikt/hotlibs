package no.nav.hjelpemidler.collections

fun <E : Enum<E>> Iterable<E>.toStringArray(): Array<out String> =
    map { it.name }.toTypedArray()

fun <E : Enum<E>> Array<out E>.toStringArray(): Array<out String> =
    map { it.name }.toTypedArray()

fun <E : Enum<E>> Iterable<E>.joinToQuotedString(): String =
    joinToString(", ") { "'$it'" }

fun <E : Enum<E>> Array<out E>.joinToQuotedString(): String =
    joinToString(", ") { "'$it'" }
