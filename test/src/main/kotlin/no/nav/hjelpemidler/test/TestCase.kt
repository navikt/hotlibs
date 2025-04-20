package no.nav.hjelpemidler.test

import org.junit.jupiter.api.Named

fun interface TestCase : () -> Unit

interface NamedTestCase<T : Any> : TestCase, Named<T>
