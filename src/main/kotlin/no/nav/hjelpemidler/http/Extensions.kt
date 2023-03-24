package no.nav.hjelpemidler.http

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.serialization.jackson.JacksonConverter
import io.ktor.serialization.jackson.jackson

fun HttpClientConfig<*>.jackson(block: ObjectMapper.() -> Unit = {}) =
    install(ContentNegotiation) {
        jackson {
            registerModule(JavaTimeModule())
            disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            block()
        }
    }

fun HttpClientConfig<*>.jackson(objectMapper: ObjectMapper) =
    install(ContentNegotiation) {
        register(ContentType.Application.Json, JacksonConverter(objectMapper, true))
    }
