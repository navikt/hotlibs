package no.nav.hjelpemidler.domain.kodeverk

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

sealed interface Brevkode {
    @get:JsonValue
    val kode: String
    val beskrivelse: String get() = kode

    companion object {
        @JvmStatic
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
        operator fun invoke(kode: String): Brevkode {
            val navSkjema = NavSkjema[kode]
            return when {
                navSkjema != null -> navSkjema
                NavSkjema.isNavSkjema(kode) -> UkjentNavSkjema(kode)
                else -> UkjentBrevkode(kode)
            }
        }
    }
}

@JvmInline
value class UkjentBrevkode(override val kode: String) : Brevkode {
    override fun toString() = kode
}

@OptIn(ExperimentalContracts::class)
val Brevkode.isUkjentBrevkode: Boolean
    get() {
        contract {
            returns(true) implies (this@isUkjentBrevkode is UkjentBrevkode)
        }
        return this is UkjentBrevkode
    }
