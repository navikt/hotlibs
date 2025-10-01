package no.nav.hjelpemidler.database.jpa

import jakarta.persistence.AttributeConverter
import no.nav.hjelpemidler.domain.ValueType

abstract class ValueTypeConverter<T : ValueType<R>, R : Comparable<R>>(private val factory: (R) -> T) :
    AttributeConverter<T, R> {
    override fun convertToDatabaseColumn(valueType: T?): R? = valueType?.value
    override fun convertToEntityAttribute(value: R?): T? = value?.let(factory)
}
