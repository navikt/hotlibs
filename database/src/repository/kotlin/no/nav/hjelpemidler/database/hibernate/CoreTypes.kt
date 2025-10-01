package no.nav.hjelpemidler.database.hibernate

import no.nav.hjelpemidler.database.jpa.AktørIdConverter
import no.nav.hjelpemidler.database.jpa.EnhetsnummerConverter
import no.nav.hjelpemidler.database.jpa.FødselsnummerConverter
import no.nav.hjelpemidler.domain.enhet.Enhetsnummer
import no.nav.hjelpemidler.domain.person.AktørId
import no.nav.hjelpemidler.domain.person.Fødselsnummer
import org.hibernate.boot.model.TypeContributions
import org.hibernate.boot.model.TypeContributor
import org.hibernate.service.ServiceRegistry
import org.hibernate.type.descriptor.java.StringJavaType

class AktørIdJavaType internal constructor() : DelegatingClassJavaType<AktørId, String>(
    type = AktørId::class,
    converter = AktørIdConverter.INSTANCE,
    delegate = StringJavaType.INSTANCE,
) {
    companion object {
        val INSTANCE = AktørIdJavaType()
    }
}

class FødselsnummerJavaType internal constructor() : DelegatingClassJavaType<Fødselsnummer, String>(
    type = Fødselsnummer::class,
    converter = FødselsnummerConverter.INSTANCE,
    delegate = StringJavaType.INSTANCE,
) {
    companion object {
        val INSTANCE = FødselsnummerJavaType()
    }
}

class EnhetsnummerJavaType internal constructor() : DelegatingClassJavaType<Enhetsnummer, String>(
    type = Enhetsnummer::class,
    converter = EnhetsnummerConverter.INSTANCE,
    delegate = StringJavaType.INSTANCE,
) {
    companion object {
        val INSTANCE = EnhetsnummerJavaType()
    }
}

class CoreTypesContributor internal constructor() : TypeContributor {
    override fun contribute(typeContributions: TypeContributions, serviceRegistry: ServiceRegistry) {
        typeContributions.contributeJavaType(AktørIdJavaType.INSTANCE)
        typeContributions.contributeJavaType(FødselsnummerJavaType.INSTANCE)
        typeContributions.contributeJavaType(EnhetsnummerJavaType.INSTANCE)
    }
}
