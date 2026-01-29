package no.nav.hjelpemidler.serialization.kotlinx

import java.util.UUID

object UUIDSerializer : ToStringSerializer<UUID>(
    serialName = "no.nav.hjelpemidler.serialization.kotlinx.UUIDSerializer",
    creator = UUID::fromString,
)
