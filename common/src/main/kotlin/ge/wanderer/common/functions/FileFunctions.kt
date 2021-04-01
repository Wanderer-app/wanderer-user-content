package ge.wanderer.common.functions

import java.net.URL

fun Any.getResourceFile(fileName: String): URL =
    this::class.java.classLoader.getResource(fileName)!!