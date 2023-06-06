package no.nav.hjelpemidler.database

import java.util.ServiceLoader

inline fun <reified S> loadService(): S =
    ServiceLoader.load(S::class.java).single()
