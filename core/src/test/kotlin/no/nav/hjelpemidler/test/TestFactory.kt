package no.nav.hjelpemidler.test

import org.junit.jupiter.api.DynamicTest
import java.util.stream.Stream

fun <T> testFactory(
    inputGenerator: Iterable<T>,
    displayNameGenerator: (T) -> String,
    testExecutor: (T) -> Unit,
): Stream<DynamicTest> = DynamicTest.stream(inputGenerator.iterator(), displayNameGenerator, testExecutor)
