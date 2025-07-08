package no.nav.hjelpemidler.streams.serialization

import com.fasterxml.jackson.module.kotlin.jacksonTypeRef
import io.confluent.kafka.streams.serdes.avro.GenericAvroSerde
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde
import no.nav.hjelpemidler.domain.person.Fødselsnummer
import no.nav.hjelpemidler.streams.kafkaSchemaRegistryConfiguration
import org.apache.avro.generic.GenericRecord
import org.apache.avro.specific.SpecificRecord
import org.apache.kafka.common.serialization.Serde
import org.apache.kafka.common.serialization.Serdes

inline fun <reified T> serde(): Serde<T> =
    Serdes.serdeFrom(T::class.java)

inline fun <reified T> jsonSerde(): Serde<T> =
    JsonSerde<T>(jacksonTypeRef<T>())

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
