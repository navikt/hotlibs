package no.nav.hjelpemidler.annotation

class AnnotationClassValue<T : Annotation>(private val annotationClass: Class<T>) : ClassValue<T>() {
    override fun computeValue(type: Class<*>): T? = type.getAnnotation(annotationClass)
    fun getOrThrow(type: Class<*>): T = get(type) ?: error("'${type.name}' lacks annotation '${annotationClass.name}'")
}

inline fun <reified T : Annotation> annotationClassValue(): AnnotationClassValue<T> =
    AnnotationClassValue<T>(T::class.java)
