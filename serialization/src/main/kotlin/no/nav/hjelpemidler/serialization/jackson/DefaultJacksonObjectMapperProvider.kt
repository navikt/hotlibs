package no.nav.hjelpemidler.serialization.jackson

import com.fasterxml.jackson.databind.ObjectMapper
import no.nav.hjelpemidler.service.LoadOrder
import no.nav.hjelpemidler.service.loadService

internal object DefaultJacksonObjectMapperProvider : JacksonObjectMapperProvider {
    private val objectMapper: ObjectMapper by lazy(::defaultJsonMapper)
    override fun invoke(): ObjectMapper = objectMapper
}

@LoadOrder(Int.MAX_VALUE)
internal class DefaultJacksonObjectMapperProviderProxy :
    JacksonObjectMapperProvider by DefaultJacksonObjectMapperProvider

val jacksonObjectMapperProvider: JacksonObjectMapperProvider by loadService()
