package no.nav.hjelpemidler.domain.person

import no.bekk.bekkopen.person.FodselsnummerCalculator
import no.nav.hjelpemidler.time.ZONE_ID_EUROPE_OSLO
import java.util.Date

private val aktørIdRange: LongRange = 1000000000000..9999999999999
fun lagTilfeldigAktørId(): AktørId = AktørId(aktørIdRange.random().toString())

fun lagTilfeldigFødselsnummer(fødselsdato: Fødselsdato): Fødselsnummer = Fødselsnummer(
    FodselsnummerCalculator
        .getFodselsnummerForDate(Date.from(fødselsdato.atStartOfDay(ZONE_ID_EUROPE_OSLO).toInstant()))
        .value
)

fun lagTilfeldigFødselsnummer(): Fødselsnummer = lagTilfeldigFødselsnummer((1..99).random().år)
