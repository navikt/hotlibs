package no.nav.hjelpemidler.database.hibernate.test

import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.SequenceGenerator
import jakarta.persistence.Table
import no.nav.hjelpemidler.domain.person.Fødselsnummer
import org.hibernate.annotations.CreationTimestamp
import java.time.Instant

@Entity
@Table(name = "sak_v1")
class TestSakEntity(
    @Enumerated(EnumType.STRING)
    val sakstype: Sakstype,
    val fnr: Fødselsnummer,
    @CreationTimestamp
    val opprettet: Instant = Instant.now(),
    @OneToMany(mappedBy = "sakId")
    val saksstatuser: List<TestSaksstatus>,
    @Id
    @GeneratedValue
    @SequenceGenerator(name = "sak_v1_id_seq", allocationSize = 1)
    val id: Long = 0,
) {
    override fun toString(): String =
        "TestSakEntity(sakstype=$sakstype, fnr=$fnr, opprettet=$opprettet, saksstatuser=[], id=$id)"
}

enum class Sakstype {
    SØKNAD, BESTILLING, BYTTE
}
