package no.nav.hjelpemidler.collections

import java.util.EnumSet

inline fun <reified E : Enum<E>> enumSet(): Set<E> =
    EnumSet.allOf(E::class.java)

inline fun <reified E : Enum<E>> emptyEnumSet(): Set<E> =
    EnumSet.noneOf(E::class.java)

fun <E : Enum<E>> enumSetOf(e: E): Set<E> =
    EnumSet.of(e)

fun <E : Enum<E>> enumSetOf(e1: E, e2: E): Set<E> =
    EnumSet.of(e1, e2)

fun <E : Enum<E>> enumSetOf(e1: E, e2: E, e3: E): Set<E> =
    EnumSet.of(e1, e2, e3)

fun <E : Enum<E>> enumSetOf(e1: E, e2: E, e3: E, e4: E): Set<E> =
    EnumSet.of(e1, e2, e3, e4)

fun <E : Enum<E>> enumSetOf(e1: E, e2: E, e3: E, e4: E, e5: E): Set<E> =
    EnumSet.of(e1, e2, e3, e4, e5)

fun <E : Enum<E>> enumSetOf(first: E, vararg rest: E): Set<E> =
    EnumSet.of(first, *rest)

inline fun <reified E : Enum<E>> Array<out String>.toEnumSet(): Set<E> {
    if (isEmpty()) return emptyEnumSet<E>()
    return EnumSet.copyOf(map { enumValueOf<E>(it) })
}

inline fun <reified E : Enum<E>> Collection<E>.toEnumSet(): Set<E> {
    if (isEmpty()) return emptyEnumSet<E>()
    return EnumSet.copyOf(this)
}

fun <E : Enum<E>> E.toSet(): Set<E> =
    EnumSet.of(this)
