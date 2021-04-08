package ge.wanderer.service.spring

import org.slf4j.Logger
import org.slf4j.LoggerFactory

internal fun Any.logger(): Logger = LoggerFactory.getLogger(this.javaClass)