package no.nav.hjelpemidler.collections

import java.util.Properties

fun propertiesOf(vararg pairs: Pair<String, String>): Properties =
    mapOf(*pairs).toProperties()
