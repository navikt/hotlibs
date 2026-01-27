package no.nav.hjelpemidler.oppgave.hendelse

import io.kotest.assertions.assertSoftly
import io.kotest.matchers.shouldBe
import no.nav.hjelpemidler.domain.enhet.Enhetsnummer
import no.nav.hjelpemidler.domain.tilgang.NavIdent
import no.nav.hjelpemidler.serialization.jackson.jsonMapper
import no.nav.hjelpemidler.serialization.jackson.readResourceAsValue
import kotlin.test.Test

class EksternOppgavehendelseTest {
    @Test
    fun `Deserialiser JSON til EksternOppgaveEvent`() {
        val oppgavehendelse = jsonMapper.readResourceAsValue<EksternOppgavehendelse>("/ekstern_oppgavehendelse_1.json")
        assertSoftly(oppgavehendelse) {
            hendelse.hendelsestype shouldBe EksternOppgavehendelse.Hendelse.Type.OPPGAVE_ENDRET

            utførtAv.id shouldBe NavIdent("Z123456")
            utførtAv.enhetsnummer shouldBe Enhetsnummer("4715")

            oppgave.oppgaveId shouldBe "987654321"
            oppgave.versjon shouldBe 3

            oppgave.kategorisering.isTemaHjelpemidler shouldBe true
            oppgave.kategorisering.isOppgavetypeHotsak shouldBe true
        }
    }
}
