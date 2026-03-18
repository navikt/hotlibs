package no.nav.hjelpemidler.database.annotation

import tools.jackson.databind.PropertyName
import tools.jackson.databind.cfg.MapperConfig
import tools.jackson.databind.introspect.Annotated
import tools.jackson.databind.introspect.NopAnnotationIntrospector

class DatabaseAnnotationIntrospector : NopAnnotationIntrospector() {
    override fun findPropertyAliases(configuration: MapperConfig<*>?, annotated: Annotated?): List<PropertyName?>? {
        val annotation = _findAnnotation(annotated, Column::class.java) ?: return null
        return null
    }
}
