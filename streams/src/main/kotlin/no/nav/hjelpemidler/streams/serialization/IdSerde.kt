package no.nav.hjelpemidler.streams.serialization

import no.nav.hjelpemidler.domain.id.Id
import org.apache.kafka.common.serialization.Deserializer
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.common.serialization.Serializer

class IdSerde<T : Id<*>>(transform: (String) -> T) : Serdes.WrapperSerde<T>(
    Serializer<T> { _, data -> data?.toByteArray() },
    Deserializer<T> { _, data -> data?.toString(Charsets.UTF_8)?.let(transform) },
)
