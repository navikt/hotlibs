package no.nav.hjelpemidler.streams.serialization

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonTypeRef
import io.confluent.kafka.streams.serdes.avro.GenericAvroSerde
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde
import no.nav.hjelpemidler.domain.id.Id
import no.nav.hjelpemidler.domain.person.Fødselsnummer
import no.nav.hjelpemidler.serialization.jackson.jsonMapper
import no.nav.hjelpemidler.streams.kafkaSchemaRegistryConfiguration
import org.apache.avro.generic.GenericRecord
import org.apache.avro.specific.SpecificRecord
import org.apache.kafka.common.serialization.Deserializer
import org.apache.kafka.common.serialization.Serde
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.common.serialization.Serializer

inline fun <reified T> serde(): Serde<T> =
    Serdes.serdeFrom(T::class.java)

class JsonSerde<T>(private val typeReference: TypeReference<T>) : Serdes.WrapperSerde<T>(
    Serializer<T> { _, data ->
        if (data == null) {
            null
        } else {
            jsonMapper.writeValueAsBytes(data)
        }
    },
    Deserializer<T> { _, data ->
        if (data == null) {
            null
        } else {
            jsonMapper.readValue<T>(data, typeReference)
        }
    },
)

inline fun <reified T> jsonSerde(): Serde<T> =
    JsonSerde<T>(jacksonTypeRef<T>())

class IdSerde<T : Id<*>>(transform: (String) -> T) : Serdes.WrapperSerde<T>(
    Serializer<T> { _, data -> data?.toByteArray() },
    Deserializer<T> { _, data -> data?.toString(Charsets.UTF_8)?.let(transform) },
)

fun fødselsnummerSerde(): Serde<Fødselsnummer> =
    IdSerde(::Fødselsnummer)

fun genericAvroSerde(): Serde<GenericRecord> =
    GenericAvroSerde().apply {
        configure(kafkaSchemaRegistryConfiguration(), false)
    }

fun <T : SpecificRecord> specificAvroSerde(): Serde<T> =
    SpecificAvroSerde<T>().apply {
        configure(kafkaSchemaRegistryConfiguration(), false)
    }
