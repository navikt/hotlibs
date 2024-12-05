package no.nav.hjelpemidler.rapids_and_rivers

import com.github.navikt.tbd_libs.kafka.AivenConfig
import com.github.navikt.tbd_libs.kafka.Config
import no.nav.hjelpemidler.configuration.Environment

fun autoConfig(): Config =
    if (Environment.current.isLocal) {
        LocalConfig.default
    } else {
        AivenConfig.default
    }
