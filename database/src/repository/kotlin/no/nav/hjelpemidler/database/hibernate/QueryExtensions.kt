package no.nav.hjelpemidler.database.hibernate

import org.hibernate.query.Query

fun <T, R> Query<T>.map(transform: (T) -> R): List<R> =
    list().map(transform)

fun <T, R, C : MutableCollection<in R>> Query<T>.mapTo(destination: C, transform: (T) -> R): C =
    list().mapTo(destination, transform)

fun <T, R : Any> Query<T>.mapNotNull(transform: (T) -> R?): List<R> =
    list().mapNotNull(transform)

fun <T, R : Any, C : MutableCollection<in R>> Query<T>.mapNotNullTo(destination: C, transform: (T) -> R?): C =
    list().mapNotNullTo(destination, transform)
