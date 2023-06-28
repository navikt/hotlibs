package no.nav.hjelpemidler.http

import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respondOk
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import io.ktor.client.statement.request
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import org.slf4j.MDC
import kotlin.test.Test

class CorrelationTest {
    @Test
    fun `Nestet MDCContext`() = runTest {
        fun valueShouldBe1() = MDC.get("id") shouldBe "1"
        fun valueShouldBe2() = MDC.get("id") shouldBe "2"

        withMDCContext(
            "id" to "1"
        ) {
            valueShouldBe1()

            withContext(Dispatchers.IO) {
                withMDCContext("id" to "2") {
                    launch {
                        valueShouldBe2()
                    }

                    valueShouldBe2()
                }
            }

            valueShouldBe1()
        }
    }

    @Test
    fun `Gjeldende correlationId skal være satt`() = runTest {
        withCorrelationId("foo" to "bar") {
            currentCorrelationId() shouldNotBe null
        }
    }

    @Test
    fun `Gjeldende correlationId skal settes som header`() = runTest {
        val engine = MockEngine {
            respondOk("test")
        }
        val client = createHttpClient(engine) {
            defaultRequest {
                correlationId()
            }
        }
        withCorrelationId("foo" to "bar") {
            val response = client.get("/test")
            val request = response.request
            val id = currentCorrelationId().shouldNotBeNull()
            request.navCallId() shouldBe id
            request.navCorrelationId() shouldBe id
            request.navConsumerId() shouldBe "hm-http"
        }
    }

    @Test
    fun `Nestet correlationId`() = runTest {
        withCorrelationId("foo" to "bar") {
            val id1 = currentCorrelationId()
            val id2 = withCorrelationId {
                currentCorrelationId()
            }
            id1 shouldBe id2
        }
    }
}
