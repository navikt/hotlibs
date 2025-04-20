package no.nav.hjelpemidler.test

import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Named
import java.util.stream.Stream
import kotlin.streams.asStream

fun <T : Any> testFactory(
    inputGenerator: Sequence<T>,
    displayNameGenerator: (T) -> String,
    testExecutor: (T) -> Unit,
): Stream<DynamicTest> =
    DynamicTest.stream(inputGenerator.asStream(), displayNameGenerator, testExecutor)

fun <T : Any> testFactory(
    inputGenerator: Sequence<Named<T>>,
    testExecutor: (T) -> Unit,
): Stream<DynamicTest> =
    DynamicTest.stream(inputGenerator.asStream(), testExecutor)

@JvmName("testFactoryExt")
fun <T : Named<T>> Sequence<T>.testFactory(testExecutor: (T) -> Unit): Stream<DynamicTest> =
    testFactory(this, testExecutor)

@JvmName("testFactoryExt")
fun <T : NamedTestCase<T>> Sequence<T>.testFactory(): Stream<DynamicTest> =
    testFactory(NamedTestCase<T>::invoke)
