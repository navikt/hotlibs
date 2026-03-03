package no.nav.hjelpemidler.database.annotation

import tools.jackson.databind.PropertyNamingStrategies

/**
 * @see [tools.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy]
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
