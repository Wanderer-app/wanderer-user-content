package ge.wanderer.common

import org.joda.time.LocalDate
import org.joda.time.LocalDateTime

fun date(string: String): LocalDate = LocalDate.parse(string)
fun dateTime(string: String): LocalDateTime = LocalDateTime.parse(string)
fun today(): LocalDate = LocalDate.now()
fun now(): LocalDateTime = LocalDateTime.now()
