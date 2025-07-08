package no.nav.hjelpemidler.streams.serialization

import com.fasterxml.jackson.core.type.TypeReference
import no.nav.hjelpemidler.serialization.jackson.jsonMapper
import org.apache.kafka.common.serialization.Deserializer
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.common.serialization.Serializer

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
