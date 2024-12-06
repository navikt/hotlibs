package no.nav.hjelpemidler.serialization.jackson

import no.nav.hjelpemidler.service.LoadOrder
import org.junit.jupiter.api.Assertions.assertInstanceOf
import kotlin.test.Test
import kotlin.test.assertSame

class JacksonObjectMapperProviderTest {
    @Test
    fun `Global jsonMapper og instans fra jacksonObjectMapperProvider skal være den samme`() {
        val jsonMapper1 = jsonMapper
        val jsonMapper2 = jacksonObjectMapperProvider()

        assertSame(jsonMapper1, jsonMapper2)
    }

    @Test
    fun `TestJacksonObjectMapperProvider har presedens over DefaultJacksonObjectMapperProviderProxy`() {
        assertInstanceOf(TestJacksonObjectMapperProvider::class.java, jacksonObjectMapperProvider)
    }
}

@LoadOrder(0)
class TestJacksonObjectMapperProvider :
    JacksonObjectMapperProvider by DefaultJacksonObjectMapperProvider
