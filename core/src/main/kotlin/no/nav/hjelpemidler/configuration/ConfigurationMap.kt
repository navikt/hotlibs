package no.nav.hjelpemidler.configuration

interface ConfigurationMap : Map<String, String> {
    fun containsKey(key: EnvironmentVariableKey): Boolean =
        containsKey(key.toString())

    operator fun contains(key: EnvironmentVariableKey): Boolean =
        contains(key.toString())

    operator fun get(key: EnvironmentVariableKey): String? =
        get(key.toString())

    fun getOrDefault(key: EnvironmentVariableKey, defaultValue: String): String =
        getOrDefault(key.toString(), defaultValue)
}
