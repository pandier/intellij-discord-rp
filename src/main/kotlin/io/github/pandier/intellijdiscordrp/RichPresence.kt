package io.github.pandier.intellijdiscordrp

import com.intellij.openapi.application.ApplicationInfo
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import de.jcm.discordgamesdk.activity.Activity
import io.github.pandier.intellijdiscordrp.icon.DefaultIconTheme
import java.time.Instant

interface RichPresence {
    val applicationInfo: ApplicationInfo
    val start: Instant

    fun toActivity(): Activity
}

open class ApplicationRichPresence(
    override val applicationInfo: ApplicationInfo = ApplicationInfo.getInstance(),
    override val start: Instant = Instant.now()
) : RichPresence {

    override fun toActivity(): Activity = Activity().apply {
        assets().largeImage = DefaultIconTheme.getPlatform(applicationInfo)
        assets().largeText = applicationInfo.fullApplicationName

        timestamps().start = start
    }
}

open class ProjectRichPresence(
    applicationInfo: ApplicationInfo = ApplicationInfo.getInstance(),
    start: Instant = Instant.now(),
    val project: Project,
) : ApplicationRichPresence(applicationInfo, start) {

    override fun toActivity(): Activity = super.toActivity().apply {
        details = project.name
    }
}

open class FileRichPresence(
    applicationInfo: ApplicationInfo = ApplicationInfo.getInstance(),
    start: Instant = Instant.now(),
    project: Project,
    val file: VirtualFile
) : ProjectRichPresence(applicationInfo, start, project) {

    override fun toActivity(): Activity = super.toActivity().apply {
        state = "Editing ${file.name}"

        assets().smallImage = assets().largeImage
        assets().smallText = assets().largeText
        assets().largeImage = DefaultIconTheme.getFile(file.fileType)
        assets().largeText = file.name
    }
}
