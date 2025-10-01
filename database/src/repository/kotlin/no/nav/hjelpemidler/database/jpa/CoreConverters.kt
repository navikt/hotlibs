package no.nav.hjelpemidler.database.jpa

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import no.nav.hjelpemidler.domain.enhet.Enhetsnummer
import no.nav.hjelpemidler.domain.id.LongId
import no.nav.hjelpemidler.domain.id.StringId
import no.nav.hjelpemidler.domain.person.AktørId
import no.nav.hjelpemidler.domain.person.Fødselsnummer

abstract class LongIdConverter<T : LongId>(private val transform: (Long) -> T) : AttributeConverter<T, Long> {
    override fun convertToDatabaseColumn(id: T?): Long? = id?.value
    override fun convertToEntityAttribute(idLong: Long?): T? = idLong?.let(transform)
}

abstract class StringIdConverter<T : StringId>(private val transform: (String) -> T) : AttributeConverter<T, String> {
    override fun convertToDatabaseColumn(id: T?): String? = id?.value
    override fun convertToEntityAttribute(idString: String?): T? = idString?.let(transform)
}

@Converter
class AktørIdConverter internal constructor() : StringIdConverter<AktørId>(::AktørId) {
    companion object {
        val INSTANCE = AktørIdConverter()
    }
}

@Converter
class FødselsnummerConverter internal constructor() : StringIdConverter<Fødselsnummer>(::Fødselsnummer) {
    companion object {
        val INSTANCE = FødselsnummerConverter()
    }
}

@Converter
class EnhetsnummerConverter internal constructor() : StringIdConverter<Enhetsnummer>(::Enhetsnummer) {
    companion object {
        val INSTANCE = EnhetsnummerConverter()
    }
}
