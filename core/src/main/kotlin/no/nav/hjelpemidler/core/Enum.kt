package no.nav.hjelpemidler.core

import kotlin.enums.enumEntries

inline fun <reified T : Enum<T>> enumValueOfOrNull(name: String): T? = enumEntries<T>().firstOrNull { it.name == name }

/**
 * Konverter fra en enum-konstant til en annen.
 *
 * @throws [IllegalArgumentException] hvis det ikke finnes en enum-konstant med [name] i [T].
 */
inline fun <reified T : Enum<T>> Enum<*>.asEnum(): T = enumValueOf<T>(name)

/**
 * Konverter fra en enum-konstant til en annen, eller null hvis det ikke finnes en enum-konstant med [name] i [T].
 */
inline fun <reified T : Enum<T>> Enum<*>.asEnumOrNull(): T? = enumValueOfOrNull<T>(name)
