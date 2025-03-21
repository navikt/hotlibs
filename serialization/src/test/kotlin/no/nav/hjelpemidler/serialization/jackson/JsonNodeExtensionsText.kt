package no.nav.hjelpemidler.serialization.jackson

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.MissingNode
import com.fasterxml.jackson.databind.node.NullNode
import com.fasterxml.jackson.databind.node.TextNode
import io.kotest.inspectors.forAll
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import no.nav.hjelpemidler.domain.person.Fødselsnummer
import no.nav.hjelpemidler.domain.person.år
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.util.UUID
import kotlin.test.Test

class JsonNodeExtensionsText {
    @Test
    fun `JsonNode til UUID`() {
        val uuid = UUID.randomUUID()
        textNodeOf(uuid).uuidValue() shouldBe uuid

        verify { uuidValueOrNull() }
    }

    @Test
    fun `JsonNode til LocalDate`() {
        val localDate = LocalDate.now()
        textNodeOf(localDate).localDateValue() shouldBe localDate

        verify { localDateValueOrNull() }
    }

    @Test
    fun `JsonNode til LocalDateTime`() {
        val localDateTime = LocalDateTime.now()
        textNodeOf(localDateTime).localDateTimeValue() shouldBe localDateTime

        verify { localDateTimeValueOrNull() }
    }

    @Test
    fun `JsonNode til Instant`() {
        val instant = Instant.now()
        textNodeOf(instant).instantValue() shouldBe instant

        verify { instantValueOrNull() }
    }

    @Test
    fun `JsonNode til ZonedDateTime`() {
        val zonedDateTime = ZonedDateTime.now()
        textNodeOf(zonedDateTime).zonedDateTime() shouldBe zonedDateTime

        verify { zonedDateTimeOrNull() }
    }

    @Test
    fun `JsonNode til Enum`() {
        val value = Foo.BAR
        textNodeOf(value).enumValue<Foo>() shouldBe value

        verify { enumValueOrNull<Foo>() }
    }

    @Test
    fun `JsonNode til Fødselsnummer`() {
        val fnr = Fødselsnummer(40.år)
        textNodeOf(fnr).fødselsnummerValue() shouldBe fnr

        verify { fødselsnummerValueOrNull() }
    }

    @Test
    fun `JsonNode til instans`() {
        val fnr = Fødselsnummer(40.år)
        textNodeOf(fnr).value<Fødselsnummer>() shouldBe fnr

        verify { valueOrNull<Fødselsnummer>() }
    }

    private fun <T> verify(block: JsonNode?.() -> T?) {
        listOf(null, NullNode.getInstance(), MissingNode.getInstance())
            .forAll {
                it.isMissingOrNull.shouldBeTrue()
                it.block().shouldBeNull()
            }
    }
}

private enum class Foo { BAR }

private fun textNodeOf(value: Any): TextNode = TextNode.valueOf(value.toString())
