package no.nav.hjelpemidler.nare.policy

@JvmName("autoriserExt")
inline fun <T : Any, R> Policy<T>.autoriser(context: T, block: (Policyevaluering) -> R): R =
    evaluer(context).let(block)

inline fun <T : Any, R> autoriser(policy: Policy<T>, context: T, block: (Policyevaluering) -> R): R =
    policy.autoriser(context, block)
