package no.nav.hjelpemidler.database.hibernate.test

import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.SequenceGenerator
import jakarta.persistence.Table
import no.nav.hjelpemidler.domain.person.Fødselsnummer
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.JdbcTypeCode
import java.time.Instant

@Entity
@Table(name = "sak_v1")
class TestSakEntity(
    @Enumerated(EnumType.STRING)
    val sakstype: Sakstype,
    @JdbcTypeCode(Fødselsnummer.SQL_TYPE)
    val fnr: Fødselsnummer,
    @CreationTimestamp
    val opprettet: Instant = Instant.now(),
    @OneToMany(mappedBy = "sakId")
    val saksstatuser: MutableList<TestSaksstatusEntity> = mutableListOf(),
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(sequenceName = "sak_v1_id_seq", allocationSize = 1)
    val id: Long = 0,
) {
    override fun toString(): String {
        return "TestSakEntity(sakstype=$sakstype, fnr=$fnr, opprettet=$opprettet, id=$id)"
    }
}

enum class Sakstype {
    SØKNAD, BESTILLING, BYTTE
}
