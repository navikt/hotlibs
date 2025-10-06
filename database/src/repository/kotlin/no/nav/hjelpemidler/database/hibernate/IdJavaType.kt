package no.nav.hjelpemidler.database.hibernate

import no.nav.hjelpemidler.domain.id.LongId
import no.nav.hjelpemidler.domain.id.StringId
import org.hibernate.type.descriptor.java.LongJavaType
import org.hibernate.type.descriptor.java.StringJavaType
import kotlin.reflect.KClass

abstract class LongIdJavaType<T : LongId>(type: KClass<T>, factory: (Long) -> T) :
    ValueJavaType<T, Long>(type, LongJavaType.INSTANCE, factory) {
    override fun getDefaultValue(): T? = factory(0)
}

abstract class StringIdJavaType<T : StringId>(type: KClass<T>, factory: (String) -> T) :
    ValueJavaType<T, String>(type, StringJavaType.INSTANCE, factory)
