package ge.wanderer.common.functions

import java.io.PrintWriter
import java.io.StringWriter

fun Exception.stackTraceString(): String {
    val sw = StringWriter()
    val pw = PrintWriter(sw)
    this.printStackTrace(pw)
    return sw.toString()
}

fun Exception.asStandardMessage(): String = this.message ?: "Exception occurred: ${this.stackTraceString()}"