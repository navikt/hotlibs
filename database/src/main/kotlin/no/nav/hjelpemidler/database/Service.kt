package no.nav.hjelpemidler.database

import org.slf4j.LoggerFactory
import java.util.ServiceLoader

inline fun <reified S> service(noinline fallback: (() -> S)? = null): Lazy<S> = lazy<S> {
    try {
        ServiceLoader.load(S::class.java).single()
    } catch (e: NoSuchElementException) {
        if (fallback == null) {
            throw e
        } else {
            LoggerFactory.getLogger(S::class.java).warn("Bruker fallback for tjeneste")
            fallback()
        }
    }
}
