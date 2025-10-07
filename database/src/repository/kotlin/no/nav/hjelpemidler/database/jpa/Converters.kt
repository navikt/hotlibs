package no.nav.hjelpemidler.database.jpa

import jakarta.persistence.Converter
import no.nav.hjelpemidler.database.repository.RepositoryConfiguration
import no.nav.hjelpemidler.domain.enhet.Enhetsnummer
import no.nav.hjelpemidler.domain.person.AktørId
import no.nav.hjelpemidler.domain.person.Fødselsnummer

@Converter
class AktørIdConverter internal constructor() : ValueTypeConverter<AktørId, String>(::AktørId)

@Converter
class FødselsnummerConverter internal constructor() : ValueTypeConverter<Fødselsnummer, String>(::Fødselsnummer)

@Converter
class EnhetsnummerConverter internal constructor() : ValueTypeConverter<Enhetsnummer, String>(::Enhetsnummer)

fun RepositoryConfiguration.defaultAttributeConverters() {
    managedClass<AktørIdConverter>()
    managedClass<FødselsnummerConverter>()
    managedClass<EnhetsnummerConverter>()
}
