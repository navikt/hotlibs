package no.nav.hjelpemidler.core

import kotlin.enums.enumEntries

inline fun <reified T : Enum<T>> enumValueOfOrNull(name: String): T? = enumEntries<T>().firstOrNull { it.name == name }
