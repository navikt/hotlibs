package no.nav.hjelpemidler.database.repository

import no.nav.hjelpemidler.database.hibernate.AktørIdJavaType
import no.nav.hjelpemidler.database.hibernate.EnhetsnummerJavaType
import no.nav.hjelpemidler.database.hibernate.FødselsnummerJavaType
import org.hibernate.boot.model.TypeContributions
import org.hibernate.boot.model.TypeContributor
import org.hibernate.service.ServiceRegistry

class RepositoryTypeContributor internal constructor() : TypeContributor {
    override fun contribute(typeContributions: TypeContributions, serviceRegistry: ServiceRegistry) =
        with(typeContributions) {
            contributeJavaType(AktørIdJavaType.INSTANCE)
            contributeJavaType(FødselsnummerJavaType.INSTANCE)
            contributeJavaType(EnhetsnummerJavaType.INSTANCE)
        }
}
