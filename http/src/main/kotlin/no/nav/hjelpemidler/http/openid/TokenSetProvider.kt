package no.nav.hjelpemidler.http.openid

fun interface TokenSetProvider {
    suspend operator fun invoke(): TokenSet
}
