package no.nav.hjelpemidler.database

import com.fasterxml.jackson.databind.PropertyNamingStrategies

/**
 * @see [com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy]
 */
internal class DatabasePropertyNamingStrategy : PropertyNamingStrategies.SnakeCaseStrategy() {
    override fun translate(input: String?): String? {
        val property = super.translate(input) ?: return null
        return property
            .replace('æ', 'e')
            .replace('ø', 'o')
            .replace('å', 'a')
    }
}
