package no.nav.hjelpemidler.database.hibernate

import no.nav.hjelpemidler.domain.enhet.Enhetsnummer
import no.nav.hjelpemidler.domain.person.AktørId
import no.nav.hjelpemidler.domain.person.Fødselsnummer
import org.hibernate.type.descriptor.WrapperOptions
import org.hibernate.type.descriptor.java.JavaType
import kotlin.reflect.KClass

class AktørIdJavaType internal constructor() :
    StringIdJavaType<AktørId>(AktørId::class, ::AktørId) {
    companion object {
        val INSTANCE = AktørIdJavaType()
    }
}

class FødselsnummerJavaType internal constructor() :
    StringIdJavaType<Fødselsnummer>(Fødselsnummer::class, ::Fødselsnummer) {
    companion object {
        val INSTANCE = FødselsnummerJavaType()
    }
}

class EnhetsnummerJavaType internal constructor() :
    StringIdJavaType<Enhetsnummer>(Enhetsnummer::class, ::Enhetsnummer) {
    companion object {
        val INSTANCE = EnhetsnummerJavaType()
    }
}

fun <T : Any, X : Any> JavaType<T>.unwrap(value: T?, type: KClass<X>, options: WrapperOptions): X? =
    unwrap(value, type.java, options)
