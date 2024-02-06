package no.nav.hjelpemidler.database.sql

import io.kotest.matchers.shouldBe
import kotliquery.Query
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.isAccessible
import kotlin.test.Test

class SqlNamedParameterTest {
    @Test
    fun `Bruker samme regex som kotliquery`() {
        regex shouldBe Query.Companion::class.declaredMemberProperties.single { it.name == "regex" }.let {
            it.isAccessible = true
            it.get(Query)
        }
    }
}
