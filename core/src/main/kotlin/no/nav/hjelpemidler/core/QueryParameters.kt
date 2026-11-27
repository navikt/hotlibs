package no.nav.hjelpemidler.core

typealias QueryParameters = Map<String, Any?>

interface QueryParametersProvider {
    fun toQueryParameters(): Map<String, Any?>
}
