package no.nav.hjelpemidler.rapids_and_rivers

import com.github.navikt.tbd_libs.rapids_and_rivers.JsonMessage
import com.github.navikt.tbd_libs.rapids_and_rivers_api.MessageProblems
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainOnly
import no.nav.hjelpemidler.domain.person.Fødselsnummer
import no.nav.hjelpemidler.domain.person.år
import java.util.UUID
import kotlin.test.Ignore
import kotlin.test.Test

class JsonMessageExtensionsTest {
    private val søknadId = UUID.randomUUID()
    private val fnr = Fødselsnummer(75.år)

    @Test
    fun `JsonMessage mangler eventId`() {
        val message = jsonMessageOf(
            "id" to "1",
            "vedtakId" to "2",
            "søknadId" to søknadId,
            "fnrBruker" to fnr,
            "eventName" to TestMessage.EVENT_NAME,
        )

        message.require<TestMessage>()

        message.problems.errors.shouldContainOnly("Missing required key eventId")
    }

    @Test
    fun `JsonMessage mangler eventName`() {
        val message = jsonMessageOf(
            "id" to "1",
            "vedtakId" to "2",
            "søknadId" to søknadId,
            "fnrBruker" to fnr,
            "eventId" to UUID.randomUUID(),
        )

        message.require<TestMessage>()

        message.problems.errors.shouldContainOnly("Missing required key eventName")
    }

    @Test
    fun `JsonMessage mangler id`() {
        val message = jsonMessageOf(
            "vedtakId" to "2",
            "søknadId" to søknadId,
            "fnrBruker" to fnr,
            "eventId" to UUID.randomUUID(),
            "eventName" to TestMessage.EVENT_NAME,
        )

        message.require<TestMessage>()

        message.problems.errors.shouldContainOnly("Missing required key id")
    }

    @Test
    @Ignore("Krever `requireAnyKey(vararg keys: String)` el. i rapids-and-rivers")
    fun `JsonMessage mangler søknadId`() {
        val message = jsonMessageOf(
            "id" to "1",
            "vedtakId" to "2",
            "fnrBruker" to fnr,
            "eventId" to UUID.randomUUID(),
            "eventName" to TestMessage.EVENT_NAME,
        )

        message.require<TestMessage>()

        message.problems.errors.shouldContainOnly("Missing required key søknadId, soknadId")
    }

    @Test
    fun `JsonMessage bruker @JsonAlias for søknadId`() {
        val message = jsonMessageOf(
            "id" to "1",
            "vedtakId" to "2",
            "soknadId" to søknadId,
            "fnrBruker" to fnr,
            "eventId" to UUID.randomUUID(),
            "eventName" to TestMessage.EVENT_NAME,
        )

        message.require<TestMessage>()

        message.problems.errors.shouldBeEmpty()
    }

    @Test
    fun `JsonMessage bruker @JsonProperty for brukerFnr`() {
        val message = jsonMessageOf(
            "id" to "1",
            "vedtakId" to "2",
            "soknadId" to søknadId,
            "brukerFnr" to fnr,
            "eventId" to UUID.randomUUID(),
            "eventName" to TestMessage.EVENT_NAME,
        )

        message.require<TestMessage>()

        message.problems.errors.shouldContainOnly("Missing required key fnrBruker")
    }

    @Test
    fun `JsonMessage mangler vedtakId`() {
        val message = jsonMessageOf(
            "id" to "1",
            "soknadId" to søknadId,
            "fnrBruker" to fnr,
            "eventId" to UUID.randomUUID(),
            "eventName" to TestMessage.EVENT_NAME,
        )

        message.require<TestMessage>()

        message.problems.errors.shouldBeEmpty()
    }
}

// NB! Feltene under er ikke public, derav reflection for å lese dem i testene her

private val JsonMessage.problems: MessageProblems
    get() = javaClass.getDeclaredField("problems").let {
        it.isAccessible = true
        it.get(this) as MessageProblems
    }

@Suppress("UNCHECKED_CAST")
private val MessageProblems.errors: List<String>
    get() = javaClass.getDeclaredField("errors").let {
        it.isAccessible = true
        it.get(this) as List<String>
    }

@Suppress("UNCHECKED_CAST")
private val MessageProblems.severe: List<String>
    get() = javaClass.getDeclaredField("severe").let {
        it.isAccessible = true
        it.get(this) as List<String>
    }
