package no.nav.hjelpemidler.serialization.jackson

import tools.jackson.databind.DatabindException
import tools.jackson.databind.DeserializationContext
import tools.jackson.databind.SerializationContext

fun SerializationContext.error(message: String): Nothing {
    throw DatabindException.from(this, message)
}

fun DeserializationContext.error(message: String): Nothing {
    throw DatabindException.from(this, message)
}
