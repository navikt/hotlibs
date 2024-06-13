package no.nav.hjelpemidler.configuration

import io.github.oshai.kotlinlogging.KotlinLogging
import java.io.File

private val log = KotlinLogging.logger {}

fun vaultSecret(filename: String): Lazy<String> = lazy {
    runCatching { File(filename).readText(Charsets.UTF_8) }.getOrElse {
        log.warn(it) { "Kunne ikke lese filename: $filename" }
        ""
    }
}
