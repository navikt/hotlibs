package no.nav.hjelpemidler.nare.test

import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.jacksonMapperBuilder

val testJsonMapper: JsonMapper = jacksonMapperBuilder().build()
