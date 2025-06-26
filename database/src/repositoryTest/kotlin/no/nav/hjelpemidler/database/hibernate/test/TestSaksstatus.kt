package no.nav.hjelpemidler.database.hibernate.test

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.SequenceGenerator
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import java.time.Instant

@Entity
@Table(name = "saksstatus_v1")
class TestSaksstatus(
    val sakId: Long,
    val saksstatus: String,
    @CreationTimestamp
    val gyldigFra: Instant = Instant.now(),
    @Id
    @GeneratedValue
    @SequenceGenerator(name = "saksstatus_v1_id_seq", allocationSize = 1)
    val id: Long = 0,
) {
    override fun toString(): String =
        "TestSaksstatus(sakId=$sakId, saksstatus='$saksstatus', gyldigFra=$gyldigFra, id=$id)"
}
