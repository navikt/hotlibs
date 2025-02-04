package no.nav.hjelpemidler.nare.policy

infix fun <T> T.kan(policy: Policy<T>): PolicyEvaluering = policy.evaluer(this)
infix fun <T> T.kanIkke(policy: Policy<T>): PolicyEvaluering = policy.evaluer(this).ikke()

inline fun <T, R> Policy<T>.krevTillatelseEllerKast(context: T, block: (PolicyEvaluering) -> R): R =
    evaluer(context).let {
        if (it.resultat == PolicyResultat.TILLAT) {
            block(it)
        } else {
            throw PolicyEvalueringException(it)
        }
    }

inline fun <T, R> Policy<T>.krevTillatelse(context: T, block: (PolicyEvaluering) -> R): Any =
    evaluer(context).let {
        if (it.resultat == PolicyResultat.TILLAT) {
            block(it)
        } else {
            it
        }
    } ?: PolicyEvaluering.nekt("Kunne ikke evaluere policy")

inline fun <T, R> krevTillatelseEllerKast(context: T, policy: Policy<T>, block: (PolicyEvaluering) -> R): R =
    policy.krevTillatelseEllerKast(context, block)

inline fun <T, R> krevTillatelse(context: T, policy: Policy<T>, block: (PolicyEvaluering) -> R): Any =
    policy.krevTillatelse(context, block)
