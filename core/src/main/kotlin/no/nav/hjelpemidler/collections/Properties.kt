package no.nav.hjelpemidler.collections

import java.io.File
import java.io.InputStream
import java.util.Properties

fun propertiesOf(vararg pairs: Pair<String, String>): Properties =
    mapOf(*pairs).toProperties()

fun propertiesOf(stream: InputStream): Properties =
    Properties().apply { load(stream.buffered()) }

fun propertiesOf(file: File): Properties =
    file.inputStream().use(::propertiesOf)
