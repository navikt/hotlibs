package no.nav.hjelpemidler.metrics

data class MetricsEvent(
    val measurementName: String,
    val fields: Map<String, Any?> = emptyMap(),
    val tags: Map<String, String?> = emptyMap(),
)
