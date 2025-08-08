package no.nav.hjelpemidler.http.openid

import com.auth0.jwt.JWT
import com.auth0.jwt.interfaces.DecodedJWT

@JvmInline
value class Token(private val value: String) {
    constructor(value: DecodedJWT) : this(value.token)

    fun decode(): DecodedJWT = JWT.decode(value)

    override fun toString(): String = value
}
