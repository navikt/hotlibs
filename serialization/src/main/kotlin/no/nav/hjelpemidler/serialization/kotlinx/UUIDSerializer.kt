package no.nav.hjelpemidler.serialization.kotlinx

import java.util.UUID

object UUIDSerializer : AbstractStringSerializer<UUID>(
    serialName = "no.nav.hjelpemidler.serialization.kotlinx.UUIDSerializer",
    toString = UUID::toString,
    fromString = UUID::fromString,
)
