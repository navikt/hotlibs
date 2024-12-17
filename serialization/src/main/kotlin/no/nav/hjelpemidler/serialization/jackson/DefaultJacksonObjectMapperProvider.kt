package no.nav.hjelpemidler.serialization.jackson

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.oshai.kotlinlogging.KotlinLogging
import no.nav.hjelpemidler.service.LoadOrder
import no.nav.hjelpemidler.service.loadService

internal object DefaultJacksonObjectMapperProvider : JacksonObjectMapperProvider {
    private val log = KotlinLogging.logger {}

    override fun invoke(): ObjectMapper {
        log.info { "Oppretter ObjectMapper" }
        return defaultJsonMapper()
    }
}

@LoadOrder(Int.MAX_VALUE)
internal class DefaultJacksonObjectMapperProviderProxy :
    JacksonObjectMapperProvider by DefaultJacksonObjectMapperProvider

internal val jacksonObjectMapperProvider: JacksonObjectMapperProvider by loadService()
