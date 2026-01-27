package no.nav.hjelpemidler.oppgave.hendelse

import com.fasterxml.jackson.annotation.JsonAlias

/**
 * @see <a href="https://github.com/navikt/oppgave/blob/master/src/main/java/no/nav/oppgave/infrastruktur/kafka/OppgaveKafkaAivenRecord.java">OppgaveKafkaAivenRecord.java</a>
 */
data class EksternOppgaveEvent(
    val hendelse: Hendelse,
    @JsonAlias("utfortAv")
    val utførtAv: UtførtAv,
    val oppgave: Oppgave,
)
