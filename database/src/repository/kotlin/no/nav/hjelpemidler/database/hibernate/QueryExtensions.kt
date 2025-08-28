package no.nav.hjelpemidler.database.hibernate

import no.nav.hjelpemidler.database.QueryParameters
import org.hibernate.query.CommonQueryContract

fun <T : CommonQueryContract> T.setParameters(parameters: QueryParameters): T {
    parameters.forEach { (name, value) -> setParameter(name, value) }
    return this
}

fun <T : CommonQueryContract> T.setParameters(vararg pairs: Pair<String, Any?>): T = setParameters(pairs.toMap())
