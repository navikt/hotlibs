package no.nav.hjelpemidler.database.jpa

import jakarta.persistence.Converter
import no.nav.hjelpemidler.domain.enhet.Enhetsnummer
import no.nav.hjelpemidler.domain.person.AktørId
import no.nav.hjelpemidler.domain.person.Fødselsnummer

@Converter
class AktørIdConverter internal constructor() : StringIdConverter<AktørId>(::AktørId)

@Converter
class FødselsnummerConverter internal constructor() : StringIdConverter<Fødselsnummer>(::Fødselsnummer)

@Converter
class EnhetsnummerConverter internal constructor() : StringIdConverter<Enhetsnummer>(::Enhetsnummer)
