package ge.wanderer.common

import java.math.BigDecimal

fun amount(integer: Int): BigDecimal = BigDecimal.valueOf(integer.toDouble()).setScale(2)
fun amount(double: Double): BigDecimal = BigDecimal.valueOf(double).setScale(2)