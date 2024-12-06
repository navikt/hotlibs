package no.nav.hjelpemidler.collections

/**
 * Strict-versjon av [Collection.singleOrNull] som kun gir `null` hvis [Collection] er tom
 * og ikke hvis den inneholder flere elementer.
 */
fun <T> Collection<T>.strictSingleOrNull(): T? =
    when (size) {
        0 -> null
        1 -> single()
        else -> error("Forventet 0 eller 1 elementer, var $size")
    }

/**
 * Strict-versjon av [Collection.singleOrNull] som kun gir `null` hvis [Collection] er tom
 * og ikke hvis den inneholder flere elementer.
 */
fun <T> Collection<T>.strictSingleOrNull(predicate: (T) -> Boolean): T? =
    filter(predicate).strictSingleOrNull()
