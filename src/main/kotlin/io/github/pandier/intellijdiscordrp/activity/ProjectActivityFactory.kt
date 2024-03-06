package io.github.pandier.intellijdiscordrp.activity

import com.intellij.openapi.application.ApplicationInfo
import com.intellij.openapi.project.Project
import de.jcm.discordgamesdk.activity.Activity
import io.github.pandier.intellijdiscordrp.icon.IconTheme
import java.time.Instant

class ProjectActivityFactory(
    private val iconTheme: IconTheme,
    private val applicationInfo: ApplicationInfo,
    private val start: Instant,
    private val project: Project,
) : ActivityFactory {

    override fun create(): Activity = Activity().apply {
        assets().largeImage = iconTheme.getPlatform(applicationInfo)
        assets().largeText = applicationInfo.fullApplicationName

        timestamps().start = start

        details = project.name
    }
}