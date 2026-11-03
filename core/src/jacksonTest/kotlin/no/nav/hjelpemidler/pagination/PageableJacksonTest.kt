package no.nav.hjelpemidler.pagination

import io.kotest.assertions.json.shouldEqualJson
import io.kotest.matchers.shouldBe
import no.nav.hjelpemidler.serialization.jackson.jsonToValue
import no.nav.hjelpemidler.serialization.jackson.valueToJson
import org.intellij.lang.annotations.Language
import org.junit.jupiter.api.Test

class PageableJacksonTest {
    private val pageable = TestPageable()

    @Language("JSON")
    private val pageableJson = """
        {
          "content": [
            "test"
          ],
          "totalElements": 1,
          "pageNumber": 1,
          "pageSize": 10,
          "hasNext": false,
          "hasPrevious": false,
          "isFirst": true,
          "isLast": true,
          "totalPages": 1
        }
    """.trimIndent()

    @Test
    fun `Serialiser til JSON med Jackson`() {
        valueToJson(TestPageable()) shouldEqualJson pageableJson
    }

    @Test
    fun `Deserialiser til Kotlin med Jackson`() {
        jsonToValue<TestPageable>(pageableJson) shouldBe pageable
    }
}

private data class TestPageable(
    override val content: List<String> = listOf("test"),
    override val totalElements: Long = 1,
    override val pageNumber: Int = 1,
    override val pageSize: Int = 10,
) : Page<String>
