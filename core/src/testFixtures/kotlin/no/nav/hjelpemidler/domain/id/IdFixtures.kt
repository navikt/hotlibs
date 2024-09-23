package no.nav.hjelpemidler.domain.id

import java.util.UUID

class TestLongId(value: Long) : LongId(value)

class TestStringId(value: String) : StringId(value)

class TestUuidId(value: UUID) : Id<UUID>(value)

val longId = TestLongId(12345)
val stringId = TestStringId("54321")
val uuidId = TestUuidId(UUID())

val longIdJsonNumber = "$longId"
val longIdJsonString = """"$longId""""
val stringIdJsonString = """"$stringId""""
val uuidIdJsonString = """"$uuidId""""
