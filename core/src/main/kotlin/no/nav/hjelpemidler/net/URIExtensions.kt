package no.nav.hjelpemidler.net

import java.net.URI

val URI.parameters: Map<String, List<String>>
    get() = query.split('&').fold(mutableMapOf()) { parameters, parameter ->
        val (key, value) = parameter.split('=', limit = 2)
        parameters.merge(key, if (value.isEmpty()) emptyList() else listOf(value), List<String>::plus)
        parameters
    }
