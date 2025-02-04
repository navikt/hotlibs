package no.nav.hjelpemidler.nare.policy

inline fun <T, R> autoriser(context: T, policy: Policy<T>, block: (PolicyEvaluering) -> R?): R? =
    policy.evaluer(context).let { block.invoke(it) }
