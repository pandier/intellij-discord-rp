package io.github.pandier.intellijdiscordrp

import com.intellij.DynamicBundle
import org.jetbrains.annotations.Nls
import org.jetbrains.annotations.PropertyKey

private const val BUNDLE = "messages.DiscordRichPresenceBundle"

internal object DiscordRichPresenceBundle {
    private val instance = DynamicBundle(DiscordRichPresenceBundle::class.java, BUNDLE)

    @Nls
    fun message(
        @PropertyKey(resourceBundle = BUNDLE) key: String,
        vararg params: Any,
    ): String {
        return instance.getMessage(key, *params)
    }
}