package no.nav.hjelpemidler.nare.policy

infix fun <T : Any> T.kan(policy: Policy<T>): Policyevaluering = policy.evaluer(this)
infix fun <T : Any> T.kanIkke(policy: Policy<T>): Policyevaluering = policy.evaluer(this).ikke()

inline fun <T : Any, R> Policy<T>.krevTillatelseEllerKast(kontekst: T, block: (Policyevaluering) -> R): R =
    evaluer(kontekst).let {
        if (it.resultat == Policyavgjørelse.TILLAT) {
            block(it)
        } else {
            throw PolicyevalueringException(it)
        }
    }

inline fun <T : Any, R> Policy<T>.krevTillatelse(kontekst: T, block: (Policyevaluering) -> R): Any =
    evaluer(kontekst).let {
        if (it.resultat == Policyavgjørelse.TILLAT) {
            block(it)
        } else {
            it
        }
    } ?: Policyevaluering.nekt("Kunne ikke evaluere policy")

inline fun <T : Any, R> krevTillatelseEllerKast(kontekst: T, policy: Policy<T>, block: (Policyevaluering) -> R): R =
    policy.krevTillatelseEllerKast(kontekst, block)

inline fun <T : Any, R> krevTillatelse(kontekst: T, policy: Policy<T>, block: (Policyevaluering) -> R): Any =
    policy.krevTillatelse(kontekst, block)
