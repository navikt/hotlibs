package no.nav.hjelpemidler.database.test

import no.nav.hjelpemidler.database.Column
import no.nav.hjelpemidler.database.Id
import no.nav.hjelpemidler.database.Table
import java.time.Instant

@Table("test_1")
data class Test1Entity(
    @Id
    @Column("id")
    val id: Long = -1,
    @Column("string")
    val string: String,
    @Column("integer")
    val integer: Int,
    @Column("enum")
    val enum: TestEnum,
    @Column("data_1")
    val data1: Map<String, Any?>,
    @Column("data_2")
    val data2: Map<String, Any?>? = null,
) {
    val notIncluded1: String = "notIncluded1"
    val notIncluded2: String get() = "notIncluded2"
}

@Table("test_2")
data class Test2Entity(
    @Id
    @Column("id")
    val id: Long = -1,
    @Column("boolean")
    val boolean: Boolean,
    @Column("instant")
    val instant: Instant,
)
