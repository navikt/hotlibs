package no.nav.hjelpemidler.nare.policy

infix fun <T : PolicyContext> T.kan(policy: Policy<T>): Policyevaluering = policy.evaluer(this)
infix fun <T : PolicyContext> T.kanIkke(policy: Policy<T>): Policyevaluering = policy.evaluer(this).ikke()

inline fun <T : Any, R> Policy<T>.krevTillatelseEllerKast(context: T, block: (Policyevaluering) -> R): R =
    evaluer(context).let {
        if (it.resultat == Policyavgjørelse.TILLAT) {
            block(it)
        } else {
            throw PolicyevalueringException(it)
        }
    }

inline fun <T : Any, R> Policy<T>.krevTillatelse(context: T, block: (Policyevaluering) -> R): Result<R> =
    evaluer(context).let {
        if (it.resultat == Policyavgjørelse.TILLAT) {
            Result.success(block(it))
        } else {
            Result.failure(PolicyevalueringException(it))
        }
    }

inline fun <T : Any, R> krevTillatelseEllerKast(context: T, policy: Policy<T>, block: (Policyevaluering) -> R): R =
    policy.krevTillatelseEllerKast(context, block)

inline fun <T : Any, R> krevTillatelse(context: T, policy: Policy<T>, block: (Policyevaluering) -> R): Result<R> =
    policy.krevTillatelse(context, block)
