package no.nav.hjelpemidler.domain.id

import java.util.UUID

class TestLongId(value: Long) : LongId(value) {
    constructor(value: String) : this(value.toLong())
}

class TestStringId(value: String) : StringId(value) {
    constructor(value: Long) : this(value.toString())
}

class TestUuidId(value: UUID) : Id<UUID>(value) {
    constructor(value: String) : this(UUID.fromString(value))
}

val numberId = TestLongId(12345)
val stringId = TestStringId("54321")
val uuidId = TestUuidId(UUID.randomUUID())

val numberIdJsonNumber = "$numberId"
val numberIdJsonString = """"$numberId""""
val stringIdJsonString = """"$stringId""""
val uuidIdJsonString = """"$uuidId""""
