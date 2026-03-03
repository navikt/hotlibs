package no.nav.hjelpemidler.serialization.jackson

import io.github.oshai.kotlinlogging.KotlinLogging
import no.nav.hjelpemidler.service.LoadOrder
import no.nav.hjelpemidler.service.loadService
import tools.jackson.databind.ObjectMapper

private val log = KotlinLogging.logger {}

/**
 * Default [JacksonObjectMapperProvider] som benyttes hvis ikke det defineres andre implementasjoner i
 * `META-INF/services/no.nav.hjelpemidler.serialization.jackson.JacksonObjectMapperProvider`
 * med lavere [LoadOrder].
 * Benytter [defaultJsonMapper] uten modifikasjon.
 *
 * @see [defaultJsonMapper]
 */
@LoadOrder(Int.MAX_VALUE)
internal class DefaultJacksonObjectMapperProvider : JacksonObjectMapperProvider {
    override fun invoke(): ObjectMapper {
        log.info { "Oppretter ObjectMapper" }
        return defaultJsonMapper()
    }
}

internal val jacksonObjectMapperProvider: JacksonObjectMapperProvider by loadService()
