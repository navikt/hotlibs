package no.nav.hjelpemidler.nare.policy

infix fun <T : PolicyContext> T.kan(policy: Policy<T>): Policyevaluering = policy.evaluer(this)
infix fun <T : PolicyContext> T.kanIkke(policy: Policy<T>): Policyevaluering = policy.evaluer(this).ikke()

@JvmName("krevTillatelseExt")
inline fun <T : Any, R> Policy<T>.krevTillatelse(context: T, block: (Policyevaluering) -> R): Result<R> =
    autoriser(context) { policyevaluering ->
        if (policyevaluering.nekt) {
            Result.failure(PolicyevalueringException(policyevaluering))
        } else {
            Result.success(block(policyevaluering))
        }
    }

@JvmName("krevTillatelseEllerKastExt")
inline fun <T : Any, R> Policy<T>.krevTillatelseEllerKast(context: T, block: (Policyevaluering) -> R): R =
    krevTillatelse(context, block).getOrThrow()

inline fun <T : Any, R> krevTillatelse(policy: Policy<T>, context: T, block: (Policyevaluering) -> R): Result<R> =
    policy.krevTillatelse(context, block)

inline fun <T : Any, R> krevTillatelseEllerKast(policy: Policy<T>, context: T, block: (Policyevaluering) -> R): R =
    policy.krevTillatelseEllerKast(context, block)
