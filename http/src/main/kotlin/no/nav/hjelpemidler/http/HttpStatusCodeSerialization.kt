package no.nav.hjelpemidler.http

import io.ktor.http.HttpStatusCode
import tools.jackson.core.JsonGenerator
import tools.jackson.core.JsonParser
import tools.jackson.databind.DeserializationContext
import tools.jackson.databind.SerializationContext
import tools.jackson.databind.deser.std.StdScalarDeserializer
import tools.jackson.databind.ser.std.StdScalarSerializer

class HttpStatusCodeSerializer : StdScalarSerializer<HttpStatusCode>(HttpStatusCode::class.java) {
    override fun serialize(value: HttpStatusCode, generator: JsonGenerator, context: SerializationContext) {
        generator.writeNumber(value.value)
    }
}

class HttpStatusCodeDeserializer : StdScalarDeserializer<HttpStatusCode>(HttpStatusCode::class.java) {
    override fun deserialize(parser: JsonParser, context: DeserializationContext): HttpStatusCode =
        HttpStatusCode.fromValue(parser.intValue)
}
