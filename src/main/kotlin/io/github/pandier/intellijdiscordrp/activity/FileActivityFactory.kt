package io.github.pandier.intellijdiscordrp.activity

import com.intellij.openapi.application.ApplicationInfo
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import de.jcm.discordgamesdk.activity.Activity
import io.github.pandier.intellijdiscordrp.icon.DefaultIconTheme
import io.github.pandier.intellijdiscordrp.icon.IconTheme
import java.time.Instant

class FileActivityFactory(
    private val iconTheme: IconTheme,
    private val applicationInfo: ApplicationInfo,
    private val start: Instant,
    private val project: Project,
    private val file: VirtualFile,
) : ActivityFactory {

    override fun create(): Activity = Activity().apply {
        assets().largeImage = DefaultIconTheme.getFile(file.fileType)
        assets().largeText = file.name
        assets().smallImage = iconTheme.getPlatform(applicationInfo)
        assets().smallText = applicationInfo.fullApplicationName

        timestamps().start = start

        details = project.name
        state = "Editing ${file.name}"
    }
}