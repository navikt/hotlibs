package no.nav.hjelpemidler.database.jpa

import jakarta.persistence.TypedQuery
import no.nav.hjelpemidler.database.QueryParameters

fun <T, R> TypedQuery<T>.map(transform: (T) -> R): List<R> =
    resultList.map(transform)

fun <T, R, C : MutableCollection<in R>> TypedQuery<T>.mapTo(destination: C, transform: (T) -> R): C =
    resultList.mapTo(destination, transform)

fun <T, R : Any> TypedQuery<T>.mapNotNull(transform: (T) -> R?): List<R> =
    resultList.mapNotNull(transform)

fun <T, R : Any, C : MutableCollection<in R>> TypedQuery<T>.mapNotNullTo(destination: C, transform: (T) -> R?): C =
    resultList.mapNotNullTo(destination, transform)

fun <T> TypedQuery<T>.setParameters(parameters: QueryParameters): TypedQuery<T> {
    parameters.forEach { (name, value) -> setParameter(name, value) }
    return this
}

fun <T> TypedQuery<T>.setParameters(vararg pairs: Pair<String, Any?>): TypedQuery<T> =
    setParameters(pairs.toMap())
