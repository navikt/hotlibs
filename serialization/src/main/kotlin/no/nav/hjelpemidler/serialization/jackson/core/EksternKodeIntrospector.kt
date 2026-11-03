package no.nav.hjelpemidler.serialization.jackson.core

import no.nav.hjelpemidler.domain.kodeverk.EksternKode
import tools.jackson.databind.cfg.MapperConfig
import tools.jackson.databind.introspect.AnnotatedClass
import tools.jackson.databind.introspect.NopAnnotationIntrospector

internal class EksternKodeIntrospector : NopAnnotationIntrospector() {
    override fun findEnumAliases(
        config: MapperConfig<*>,
        annotatedClass: AnnotatedClass,
        enumValues: Array<Enum<*>>,
        aliases: Array<Array<String>?>,
    ) {
        enumValues.forEachIndexed { index, value ->
            val eksternKode = EksternKode.orNull(value) ?: return@forEachIndexed
            aliases[index] = (aliases[index] ?: emptyArray()) + eksternKode
        }
    }
}
