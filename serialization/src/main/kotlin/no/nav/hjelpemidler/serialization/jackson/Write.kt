package no.nav.hjelpemidler.serialization.jackson

fun valueToJson(value: Any?): String = jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(value)
