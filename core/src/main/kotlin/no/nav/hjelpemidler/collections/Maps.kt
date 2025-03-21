package no.nav.hjelpemidler.collections

import java.util.EnumMap

inline fun <reified K : Enum<K>, V> enumMapOf(vararg pairs: Pair<K, V>): Map<K, V> =
    EnumMap<K, V>(K::class.java).apply { putAll(pairs) }

@Suppress("UNCHECKED_CAST")
fun <K, V> Map<K, V?>.filterNotNull(): Map<K, V> =
    filterNot { it.value == null } as Map<K, V>

fun <K : Any, V : Any> mapOfNotNull(pair: Pair<K, V?>): Map<K, V> {
    val (key, value) = pair
    return if (value == null) emptyMap() else mapOf(key to value)
}

fun <K : Any, V : Any> mapOfNotNull(vararg pairs: Pair<K, V?>): Map<K, V> =
    pairs.mapNotNull { (key, value) -> if (value == null) null else key to value }.toMap()


fun Map<String, Any?>.joinToString(
    separator: CharSequence = ", ",
    prefix: CharSequence = "",
    postfix: CharSequence = "",
): String = entries.joinToString(separator, prefix, postfix) { "${it.key}: ${it.value}" }
