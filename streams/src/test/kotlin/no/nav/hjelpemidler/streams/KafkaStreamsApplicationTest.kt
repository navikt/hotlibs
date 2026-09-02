package no.nav.hjelpemidler.streams

import io.kotest.matchers.shouldBe
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import io.ktor.server.config.MapApplicationConfig
import io.ktor.server.plugins.di.dependencies
import io.ktor.server.testing.testApplication
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import no.nav.hjelpemidler.streams.serialization.serde
import org.apache.kafka.streams.KafkaStreams
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.kstream.Consumed
import org.apache.kafka.streams.kstream.Produced
import org.junit.jupiter.api.Test

class KafkaStreamsApplicationTest {
    @Test
    fun `Applikasjon starter uten feil`() = testApplication {
        environment { config = MapApplicationConfig() }
        application {
            dependencies {
                provide<KafkaStreams> {
                    mockk<KafkaStreams> {
                        every { setStateListener(any()) } just Runs
                        every { metrics() } returns emptyMap()
                        every { state() } returns KafkaStreams.State.RUNNING
                        every { cleanUp() } just Runs
                        every { start() } just Runs
                        every { close() } just Runs
                    }
                }
            }
            main("test-application", KafkaStreamsApplicationBuilder().apply {
                topology {
                    testTopology()
                }
            })
        }

        var response = client.get("/isalive")
        response.status shouldBe HttpStatusCode.OK

        response = client.get("/isready")
        response.status shouldBe HttpStatusCode.OK
    }
}

fun StreamsBuilder.testTopology() {
    val serde = serde<String>()
    this
        .stream("test-input-topic", Consumed.with(serde, serde))
        .to("test-output-topic", Produced.with(serde, serde))
}
