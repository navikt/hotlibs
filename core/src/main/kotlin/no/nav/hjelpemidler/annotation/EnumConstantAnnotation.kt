package no.nav.hjelpemidler.annotation

class EnumConstantAnnotation<A : Annotation>(private val annotationClass: Class<A>) {
    private val perEnumClass = object : ClassValue<List<A?>>() {
        override fun computeValue(type: Class<*>): List<A?> =
            type.enumConstants.map { constant ->
                type.getField((constant as Enum<*>).name).getAnnotation(annotationClass)
            }
    }

    fun get(value: Enum<*>): A? = perEnumClass.get(value.declaringJavaClass)[value.ordinal]

    fun getOrThrow(value: Enum<*>): A = get(value)
        ?: error("'${value.declaringJavaClass.name}.${value.name}' lacks annotation '${annotationClass.name}'")
}

inline fun <reified A : Annotation> enumConstantAnnotation(): EnumConstantAnnotation<A> =
    EnumConstantAnnotation(A::class.java)
