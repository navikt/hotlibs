package no.nav.hjelpemidler.domain.serialization

import tools.jackson.databind.DatabindException
import tools.jackson.databind.DeserializationContext
import tools.jackson.databind.SerializationContext

internal fun SerializationContext.error(message: String): Nothing {
    throw DatabindException.from(this, message)
}

internal fun DeserializationContext.error(message: String): Nothing {
    throw DatabindException.from(this, message)
}
