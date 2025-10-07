package no.nav.hjelpemidler.database.jpa

import jakarta.persistence.AttributeConverter
import no.nav.hjelpemidler.domain.ValueType

abstract class ValueTypeConverter<X : ValueType<Y>, Y : Any>(private val factory: (Y) -> X) :
    AttributeConverter<X, Y> {
    override fun convertToDatabaseColumn(valueType: X?): Y? = valueType?.value
    override fun convertToEntityAttribute(value: Y?): X? = value?.let(factory)
}
