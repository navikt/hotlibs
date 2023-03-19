package no.nav.hjelpemidler.database.test

import java.time.Instant

data class Test1Entity(
    val id: Long = -1,
    val string: String,
    val integer: Int,
    val enum: TestEnum,
    val data1: Map<String, Any?>,
    val data2: Map<String, Any?>? = null,
) {
    val notIncluded1: String = "notIncluded1"
    val notIncluded2: String get() = "notIncluded2"
}

data class Test2Entity(
    val id: Long = -1,
    val boolean: Boolean,
    val instant: Instant,
)
