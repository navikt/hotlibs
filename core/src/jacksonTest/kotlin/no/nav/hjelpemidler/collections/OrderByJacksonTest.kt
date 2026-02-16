package no.nav.hjelpemidler.collections

import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.module.kotlin.jacksonMapperBuilder
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.matchers.shouldBe
import no.nav.hjelpemidler.text.doubleQuoted
import kotlin.test.Test

class OrderByJacksonTest {
    private val jsonMapper = jacksonMapperBuilder().enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS).build()

    private fun jsonToDirection(value: String) = jsonMapper.readValue<OrderBy.Direction>(value.doubleQuoted())

    @Test
    fun `Lower case verdier kan deserialiseres`() {
        jsonToDirection("asc") shouldBe OrderBy.Direction.ASCENDING
        jsonToDirection("asc_nulls_first") shouldBe OrderBy.Direction.ASCENDING_NULLS_FIRST
        jsonToDirection("desc") shouldBe OrderBy.Direction.DESCENDING
        jsonToDirection("descending_nulls_last") shouldBe OrderBy.Direction.DESCENDING_NULLS_LAST
    }
}
