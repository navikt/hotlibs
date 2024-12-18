package no.nav.hjelpemidler.serialization.jackson

import com.fasterxml.jackson.databind.ObjectMapper
import no.nav.hjelpemidler.service.LoadOrder
import org.junit.jupiter.api.Assertions.assertInstanceOf
import kotlin.test.Test

class JacksonObjectMapperProviderTest {
    @Test
    fun `TestJacksonObjectMapperProvider har presedens over DefaultJacksonObjectMapperProviderProxy`() {
        assertInstanceOf(TestJacksonObjectMapperProvider::class.java, jacksonObjectMapperProvider)
    }
}

@LoadOrder(0)
class TestJacksonObjectMapperProvider : JacksonObjectMapperProvider {
    override fun invoke(): ObjectMapper = defaultJsonMapper()
}
