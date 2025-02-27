package no.nav.hjelpemidler.nare.policy

@JvmName("harTillatelseExt")
inline fun <T : Any> Policy<T>.harTillatelse(context: T, block: (Policyevaluering) -> Unit = {}): Boolean =
    !evaluer(context).also(block).nekt

inline fun <T : Any> harTillatelse(policy: Policy<T>, context: T, block: (Policyevaluering) -> Unit = {}): Boolean =
    policy.harTillatelse(context, block)
