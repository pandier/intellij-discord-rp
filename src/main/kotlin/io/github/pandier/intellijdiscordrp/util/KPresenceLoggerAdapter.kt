package io.github.pandier.intellijdiscordrp.util

import com.intellij.openapi.diagnostic.logger
import io.github.vyfor.kpresence.logger.ILogger

object KPresenceLoggerAdapter : ILogger {
    private val logger = logger<KPresenceLoggerAdapter>()

    override fun debug(message: String) {
        logger.debug(message)
    }

    override fun error(message: String) {
        logger.error(message)
    }

    override fun error(message: String, throwable: Throwable) {
        logger.error(message, throwable)
    }

    override fun info(message: String) {
        logger.info(message)
    }

    override fun trace(message: String) {
        logger.trace(message)
    }

    override fun warn(message: String) {
        logger.warn(message)
    }
}