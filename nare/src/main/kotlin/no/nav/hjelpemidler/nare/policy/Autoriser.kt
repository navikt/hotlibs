package no.nav.hjelpemidler.nare.policy

inline fun <T : Any, R> autoriser(context: T, policy: Policy<T>, block: (Policyevaluering) -> R): R =
    policy.evaluer(context).let { block.invoke(it) }
