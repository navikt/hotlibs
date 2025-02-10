package no.nav.hjelpemidler.nare.policy

class PolicyevalueringException(
    evaluering: Policyevaluering,
    cause: Throwable? = null,
) : RuntimeException("Evaluering av policy feilet med: ${evaluering.begrunnelse}", cause)
