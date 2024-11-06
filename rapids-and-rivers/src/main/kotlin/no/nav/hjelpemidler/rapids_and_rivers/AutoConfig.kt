package no.nav.hjelpemidler.rapids_and_rivers

import com.github.navikt.tbd_libs.kafka.AivenConfig
import com.github.navikt.tbd_libs.kafka.Config
import no.nav.hjelpemidler.configuration.Environment

fun AutoConfig(): Config {
    return when (Environment.current.isLocal) {
        true -> LocalConfig.default
        false -> AivenConfig.default
    }
}
