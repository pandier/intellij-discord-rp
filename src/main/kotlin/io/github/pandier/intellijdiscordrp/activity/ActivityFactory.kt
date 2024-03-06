package io.github.pandier.intellijdiscordrp.activity

import de.jcm.discordgamesdk.activity.Activity

interface ActivityFactory {
    fun create(): Activity
}