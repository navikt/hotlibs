package no.nav.hjelpemidler.rapids_and_rivers

import com.github.navikt.tbd_libs.rapids_and_rivers.JsonMessage
import no.nav.hjelpemidler.serialization.jackson.uuidValue
import java.util.UUID

val JsonMessage.eventId: UUID get() = this["eventId"].uuidValue()
val JsonMessage.eventName: String get() = this["eventName"].textValue()
