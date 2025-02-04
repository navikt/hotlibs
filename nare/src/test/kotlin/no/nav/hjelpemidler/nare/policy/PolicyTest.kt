package no.nav.hjelpemidler.nare.policy

import no.nav.hjelpemidler.nare.core.ikke
import org.junit.jupiter.api.Test

class PolicyTest {
    private val policy1 = Policy<String>("foo", "foo") { context ->
        if (context == "foo") {
            tillat("'$context' == 'foo'")
        } else {
            nekt("'$context' != 'foo'")
        }
    }
    private val policy2 = Policy<String>("bar", "bar") { context ->
        if (context == "bar") {
            tillat("'$context' == 'bar'")
        } else {
            nekt("'$context' != 'bar'")
        }
    }
    private val policy3 = (policy1 og policy2).med("foo_og_bar", "foo_og_bar")
    private val policy4 = (policy1 eller policy2).med("foo_eller_bar", "foo_eller_bar")

    @Test
    fun `foo bar`() {
        val resultat1 = policy1.evaluer("foo")
        val resultat2 = policy1.evaluer("bar")

        println(resultat1)
        println(resultat2)

        val resultat3 = policy3.evaluer("foo")
        val resultat4 = policy3.evaluer("bar")

        println(resultat3)
        println(resultat4)

        val resultat5 = policy4.evaluer("foo")
        val resultat6 = policy4.evaluer("bar")

        println(resultat5)
        println(resultat6)

        println(ikke(policy1).evaluer("foo"))
    }
}
