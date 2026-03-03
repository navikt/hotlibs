package no.nav.hjelpemidler.database.annotation

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside
import tools.jackson.databind.annotation.JsonNaming

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@JacksonAnnotationsInside
@JsonNaming(DatabasePropertyNamingStrategy::class)
annotation class JdbcResult
