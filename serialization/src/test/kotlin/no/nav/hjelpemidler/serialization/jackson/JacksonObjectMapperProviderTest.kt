package no.nav.hjelpemidler.serialization.jackson

import io.kotest.matchers.types.shouldBeInstanceOf
import no.nav.hjelpemidler.service.LoadOrder
import tools.jackson.databind.ObjectMapper
import kotlin.test.Test

class JacksonObjectMapperProviderTest {
    @Test
    fun `TestJacksonObjectMapperProvider har presedens over DefaultJacksonObjectMapperProviderProxy`() {
        jacksonObjectMapperProvider.shouldBeInstanceOf<TestJacksonObjectMapperProvider>()
    }
}

@LoadOrder(0)
class TestJacksonObjectMapperProvider : JacksonObjectMapperProvider {
    override fun invoke(): ObjectMapper = defaultJsonMapper()
}
