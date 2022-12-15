package no.nav.hjelpemidler.database.test

import kotlin.test.assertEquals

infix fun <T> T.shouldBe(expected: T): Unit =
    assertEquals(expected, this)
