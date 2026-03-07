package io.github.pandier.intellijdiscordrp.util

import com.intellij.openapi.diagnostic.logger
import io.github.pandier.kpresence.logger.KPresenceLogger

object KPresenceLoggerAdapter : KPresenceLogger {
    private val logger = logger<KPresenceLoggerAdapter>()

    override fun error(message: String, throwable: Throwable?) {
        logger.warn(message, throwable)
    }

    override fun warn(message: String, throwable: Throwable?) {
        logger.warn(message, throwable)
    }

    override fun info(message: String, throwable: Throwable?) {
        logger.info(message, throwable)
    }

    override fun debug(message: String, throwable: Throwable?) {
        logger.debug(message, throwable)
    }
}