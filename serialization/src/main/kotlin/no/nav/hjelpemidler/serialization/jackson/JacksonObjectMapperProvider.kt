package no.nav.hjelpemidler.serialization.jackson

import com.fasterxml.jackson.databind.ObjectMapper

/**
 * SPI for å tilby [ObjectMapper].
 */
interface JacksonObjectMapperProvider {
    operator fun invoke(): ObjectMapper
}
