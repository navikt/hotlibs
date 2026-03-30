package no.nav.hjelpemidler.time

import org.threeten.extra.Days
import org.threeten.extra.Hours
import org.threeten.extra.Minutes
import org.threeten.extra.Months
import org.threeten.extra.Seconds
import org.threeten.extra.Weeks
import org.threeten.extra.Years

val Int.sekunder: Seconds get() = Seconds.of(this)
val Int.minutter: Minutes get() = Minutes.of(this)
val Int.timer: Hours get() = Hours.of(this)
val Int.dager: Days get() = Days.of(this)
val Int.uker: Weeks get() = Weeks.of(this)
val Int.måneder: Months get() = Months.of(this)
val Int.år: Years get() = Years.of(this)

val Int.arbeidsdager: Arbeidsdager get() = Arbeidsdager.of(this)
