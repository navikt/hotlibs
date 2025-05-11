package no.nav.hjelpemidler.serialization.jackson

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.BeanDescription
import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.module.kotlin.jacksonTypeRef
import java.util.concurrent.ConcurrentHashMap

object JacksonIntrospector {
    val javaTypeCache: MutableMap<TypeReference<*>, JavaType> = ConcurrentHashMap()
    val beanDescriptionForSerializationCache: MutableMap<JavaType, BeanDescription> = ConcurrentHashMap()
    val beanDescriptionForDeserializationCache: MutableMap<JavaType, BeanDescription> = ConcurrentHashMap()
    val beanDescriptionForCreationCache: MutableMap<JavaType, BeanDescription> = ConcurrentHashMap()

    inline fun <reified T> constructType(): JavaType =
        javaTypeCache.computeIfAbsent(jacksonTypeRef<T>()) {
            jsonMapper.constructType(it)
        }

    inline fun <reified T> introspectForSerialization(): BeanDescription =
        beanDescriptionForSerializationCache.computeIfAbsent(constructType<T>()) {
            jsonMapper.serializationConfig.introspect(it)
        }

    inline fun <reified T> introspectForDeserialization(): BeanDescription =
        beanDescriptionForDeserializationCache.computeIfAbsent(constructType<T>()) {
            jsonMapper.deserializationConfig.introspect(it)
        }

    inline fun <reified T> introspectForCreation(): BeanDescription =
        beanDescriptionForCreationCache.computeIfAbsent(constructType<T>()) {
            jsonMapper.deserializationConfig.introspectForCreation(it)
        }
}
