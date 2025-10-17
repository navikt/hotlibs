package no.nav.hjelpemidler.database.annotation

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside
import com.fasterxml.jackson.databind.annotation.JsonNaming
import no.nav.hjelpemidler.database.DatabasePropertyNamingStrategy

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@JacksonAnnotationsInside
@JsonNaming(DatabasePropertyNamingStrategy::class)
annotation class Table(val name: String)
