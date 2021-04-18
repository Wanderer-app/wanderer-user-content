package ge.wanderer.common.functions

import java.math.BigDecimal
import java.math.RoundingMode

fun amount(integer: Int): BigDecimal = BigDecimal.valueOf(integer.toDouble()).setScale(2)
fun amount(double: Double): BigDecimal = BigDecimal.valueOf(double).setScale(2)
fun zeroAmount(): BigDecimal = BigDecimal.valueOf(0).setScale(2)

fun BigDecimal.percentOf(other: BigDecimal): BigDecimal =
    if (other == zeroAmount()) {
        zeroAmount()
    } else {
        this
            .multiply(amount(100))
            .divide(other, 2, RoundingMode.HALF_UP)
    }
