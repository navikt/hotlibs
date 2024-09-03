package no.nav.hjelpemidler.localization

import no.nav.hjelpemidler.time.ZONE_ID_EUROPE_OSLO
import java.time.format.DateTimeFormatter
import java.util.Locale

val LOCALE_NORWAY: Locale = Locale.of("nb", "NO")
val LOCALE_NORWEGIAN: Locale = LOCALE_NORWAY

val DATE_TIME_FORMATTER_NORWEGIAN_DATE: DateTimeFormatter =
    DateTimeFormatter.ofPattern("dd.MM.yyyy", LOCALE_NORWAY).withZone(ZONE_ID_EUROPE_OSLO)
