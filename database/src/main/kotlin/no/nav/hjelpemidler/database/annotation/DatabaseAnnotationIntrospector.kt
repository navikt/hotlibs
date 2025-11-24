package no.nav.hjelpemidler.database.annotation

import com.fasterxml.jackson.databind.PropertyName
import com.fasterxml.jackson.databind.introspect.Annotated
import com.fasterxml.jackson.databind.introspect.NopAnnotationIntrospector

class DatabaseAnnotationIntrospector : NopAnnotationIntrospector() {
    override fun findPropertyAliases(annotated: Annotated?): List<PropertyName>? {
        val annotation = _findAnnotation(annotated, Column::class.java) ?: return null
        return null
    }
}
