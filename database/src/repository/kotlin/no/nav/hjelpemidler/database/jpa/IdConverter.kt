package no.nav.hjelpemidler.database.jpa

import no.nav.hjelpemidler.domain.id.LongId
import no.nav.hjelpemidler.domain.id.StringId

abstract class LongIdConverter<T : LongId>(factory: (Long) -> T) : ValueTypeConverter<T, Long>(factory)

abstract class StringIdConverter<T : StringId>(factory: (String) -> T) : ValueTypeConverter<T, String>(factory)
