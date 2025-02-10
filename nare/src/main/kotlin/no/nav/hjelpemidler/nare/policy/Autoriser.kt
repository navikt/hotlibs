package no.nav.hjelpemidler.nare.policy

inline fun <T : Any, R> autoriser(kontekst: T, policy: Policy<T>, block: (Policyevaluering) -> R?): R? =
    policy.evaluer(kontekst).let { block.invoke(it) }
