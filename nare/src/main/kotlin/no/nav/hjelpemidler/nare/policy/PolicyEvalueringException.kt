package no.nav.hjelpemidler.nare.policy

class PolicyEvalueringException(
    evaluering: PolicyEvaluering,
    cause: Throwable? = null,
) : RuntimeException("Evaluering av policy feilet med: ${evaluering.begrunnelse}", cause)
