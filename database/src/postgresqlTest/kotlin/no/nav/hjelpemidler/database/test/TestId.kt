package no.nav.hjelpemidler.database.test

import com.fasterxml.jackson.annotation.JsonCreator
import no.nav.hjelpemidler.domain.id.Id

class TestId @JsonCreator constructor(override val value: Long = 0) : Id<Long>
