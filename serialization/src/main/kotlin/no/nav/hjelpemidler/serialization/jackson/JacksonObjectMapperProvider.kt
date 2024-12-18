package no.nav.hjelpemidler.serialization.jackson

import com.fasterxml.jackson.databind.ObjectMapper

/**
 * SPI for å tilby [ObjectMapper] til applikasjoner. Skal gjøre det mulig å definere én [ObjectMapper] som benyttes
 * av applikasjonen og hotlibs/database, hotlibs/http osv.
 *
 * @see [java.util.ServiceLoader]
 */
interface JacksonObjectMapperProvider {
    operator fun invoke(): ObjectMapper
}
