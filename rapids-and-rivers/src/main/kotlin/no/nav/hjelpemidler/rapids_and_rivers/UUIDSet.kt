package no.nav.hjelpemidler.rapids_and_rivers

import java.util.UUID

fun uuidSetOf(vararg elements: String): Set<UUID> = when (elements.size) {
    0 -> emptySet()
    1 -> setOf(UUID.fromString(elements[0]))
    else -> elements.mapTo(sortedSetOf(), UUID::fromString)
}
