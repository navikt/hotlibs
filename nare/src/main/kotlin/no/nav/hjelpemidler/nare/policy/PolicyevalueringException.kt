package no.nav.hjelpemidler.nare.policy

class PolicyevalueringException(
    policyevaluering: Policyevaluering,
    cause: Throwable? = null,
) : RuntimeException("Evaluering av policy feilet med: ${policyevaluering.begrunnelse}", cause)
