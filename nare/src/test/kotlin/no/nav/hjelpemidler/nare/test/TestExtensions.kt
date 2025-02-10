package no.nav.hjelpemidler.nare.test

import io.kotest.matchers.shouldBe
import no.nav.hjelpemidler.nare.core.LogiskOperand
import no.nav.hjelpemidler.nare.evaluering.Evaluering
import no.nav.hjelpemidler.nare.evaluering.Operator

infix fun <T : LogiskOperand<T>> Evaluering<T>.harResultat(expected: T) = resultat shouldBe expected
infix fun <T : LogiskOperand<T>> Evaluering<T>.harBegrunnelse(expected: String) = begrunnelse shouldBe expected
infix fun <T : LogiskOperand<T>> Evaluering<T>.harOperator(expected: Operator) = operator shouldBe expected
