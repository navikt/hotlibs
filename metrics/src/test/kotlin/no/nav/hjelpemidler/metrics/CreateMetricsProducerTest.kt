package no.nav.hjelpemidler.metrics

import io.kotest.matchers.collections.shouldBeSingleton
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class CreateMetricsProducerTest {
    private val measurementName = "test"

    @Test
    fun `Skal opprette MetricsProducer`() = runTest {
        createMetricsProducer()
            .shouldBeInstanceOf<InMemoryMetricsProducer>()
            .use {
                it.writeEvent(measurementName)
                it.shouldBeSingleton {
                    it.measurementName shouldBe measurementName
                }
            }
    }
}
