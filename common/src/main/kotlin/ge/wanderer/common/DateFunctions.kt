package ge.wanderer.common

import org.joda.time.LocalDate

fun date(string: String): LocalDate = LocalDate.parse(string)
fun today(): LocalDate = LocalDate.now()