package no.nav.hjelpemidler.domain.id

import java.util.UUID

class TestNumberId(value: Long) : NumberId(value) {
    constructor(value: String) : this(value.toLong())
}

class TestStringId(value: String) : StringId(value) {
    constructor(value: Long) : this(value.toString())
}

class TestUuidId(value: UUID) : Id<UUID>(value) {
    constructor(value: String) : this(UUID.fromString(value))
}

val numberId = TestNumberId(12345)
val stringId = TestStringId("54321")
val uuidId = TestUuidId(UUID.randomUUID())

val numberIdJsonNumber = "$numberId"
val numberIdJsonString = """"$numberId""""
val stringIdJsonString = """"$stringId""""
val uuidIdJsonString = """"$uuidId""""

val eksternId = EksternId(
    application = "hotsak",
    resource = "saksnotat",
    parameters = mapOf(
        "sakId" to listOf("1"),
        "saksnotatId" to listOf("2"),
        "brevsendingId" to emptyList(),
    )
)
