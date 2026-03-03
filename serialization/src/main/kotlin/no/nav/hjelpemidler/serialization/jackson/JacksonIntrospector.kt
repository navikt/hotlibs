package no.nav.hjelpemidler.serialization.jackson

import tools.jackson.core.type.TypeReference
import tools.jackson.databind.BeanDescription
import tools.jackson.databind.JavaType
import tools.jackson.databind.introspect.ClassIntrospector
import tools.jackson.module.kotlin.jacksonTypeRef
import java.util.concurrent.ConcurrentHashMap

object JacksonIntrospector {
    val javaTypeCache: MutableMap<TypeReference<*>, JavaType> = ConcurrentHashMap()
    val beanDescriptionForSerializationCache: MutableMap<JavaType, BeanDescription> = ConcurrentHashMap()
    val beanDescriptionForDeserializationCache: MutableMap<JavaType, BeanDescription> = ConcurrentHashMap()
    val beanDescriptionForCreationCache: MutableMap<JavaType, BeanDescription> = ConcurrentHashMap()
    val introspector: ClassIntrospector = jsonMapper.serializationConfig().classIntrospectorInstance()

    inline fun <reified T> constructType(): JavaType =
        javaTypeCache.computeIfAbsent(jacksonTypeRef<T>()) {
            jsonMapper.constructType(it)
        }

    inline fun <reified T> introspectForSerialization(): BeanDescription =
        beanDescriptionForSerializationCache.computeIfAbsent(constructType<T>()) {
            introspector.introspectForSerialization(it, introspector.introspectClassAnnotations(it))
        }

    inline fun <reified T> introspectForDeserialization(): BeanDescription =
        beanDescriptionForDeserializationCache.computeIfAbsent(constructType<T>()) {
            introspector.introspectForDeserialization(it, introspector.introspectClassAnnotations(it))
        }

    inline fun <reified T> introspectForCreation(): BeanDescription =
        beanDescriptionForCreationCache.computeIfAbsent(constructType<T>()) {
            introspector.introspectForCreation(it, introspector.introspectClassAnnotations(it))
        }
}
