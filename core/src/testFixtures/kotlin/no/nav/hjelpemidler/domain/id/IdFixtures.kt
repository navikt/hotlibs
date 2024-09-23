package no.nav.hjelpemidler.domain.id

import com.fasterxml.jackson.annotation.JsonValue
import kotlinx.serialization.Serializable
import java.util.UUID

@JvmInline
@Serializable
value class TestLongId(override val value: Long) : Id<Long> {
    @JsonValue
    override fun toString(): String = value.toString()
}

@JvmInline
@Serializable
value class TestStringId(override val value: String) : Id<String> {
    override fun toString(): String = value
}

@JvmInline
@Serializable
value class TestUuidId(@Serializable(with = UUIDSerializer::class) override val value: UUID) : Id<UUID> {
    override fun toString(): String = value.toString()
}

val longId = TestLongId(12345)
val stringId = TestStringId("54321")
val uuidId = TestUuidId(UUID())

val longIdJsonNumber = "$longId"
val longIdJsonString = """"$longId""""
val stringIdJsonString = """"$stringId""""
val uuidIdJsonString = """"$uuidId""""
