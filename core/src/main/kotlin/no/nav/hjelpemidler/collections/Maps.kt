package no.nav.hjelpemidler.collections

import java.util.EnumMap

inline fun <reified K : Enum<K>, V> enumMapOf(vararg pairs: Pair<K, V>): Map<K, V> =
    EnumMap<K, V>(K::class.java).apply { putAll(pairs) }

fun <K, V> Map<K, V?>.filterNotNull(): Map<K, V> =
    mapNotNull { (key, value) -> value?.let { key to it } }.toMap()

fun <K : Any, V : Any> mapOfNotNull(pair: Pair<K, V?>): Map<K, V> {
    val (key, value) = pair
    return if (value == null) emptyMap() else mapOf(key to value)
}

fun <K : Any, V : Any> mapOfNotNull(vararg pairs: Pair<K, V?>): Map<K, V> =
    pairs.mapNotNull { (key, value) -> value?.let { key to it } }.toMap()

fun Map<String, Any?>.joinToString(
    separator: CharSequence = ", ",
    prefix: CharSequence = "",
    postfix: CharSequence = "",
): String = entries.joinToString(separator, prefix, postfix) { "${it.key}: ${it.value}" }

fun Map<String, List<String>>.toQueryString(): String {
    if (isEmpty()) return ""
    return entries.joinToString("&", prefix = "?") { (key, value) ->
        if (value.isEmpty()) {
            "$key="
        } else {
            value.joinToString("&") { "$key=$it" }
        }
    }
}
