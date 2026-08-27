package no.nav.hjelpemidler.kafka

import no.nav.hjelpemidler.annotation.annotationClassValue

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class KafkaEvent(
    /**
     * `eventName` som skal brukes i melding.
     */
    val name: String,
    /**
     * Andre `eventName` for melding, f.eks. tidligere brukte verdier.
     */
    vararg val alternativeNames: String,
) {
    companion object {
        const val NAME_KEY: String = "eventName"

        private val classValue = annotationClassValue<KafkaEvent>()
        fun <T : KafkaMessage> of(type: Class<T>): KafkaEvent = classValue.getOrThrow(type)
    }
}
