package no.nav.hjelpemidler.http

import io.ktor.http.HttpStatusCode

interface HttpStatusCodeProvider {
    val status: HttpStatusCode
}
