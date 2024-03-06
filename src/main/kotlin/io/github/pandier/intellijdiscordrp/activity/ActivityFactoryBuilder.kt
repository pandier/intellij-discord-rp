package io.github.pandier.intellijdiscordrp.activity

import com.intellij.openapi.application.ApplicationInfo
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import io.github.pandier.intellijdiscordrp.icon.DefaultIconTheme
import io.github.pandier.intellijdiscordrp.icon.IconTheme
import io.github.pandier.intellijdiscordrp.service.timeTrackingService
import java.time.Instant

@Suppress("MemberVisibilityCanBePrivate")
class ActivityFactoryBuilder(
    private var project: Project,
) {
    var iconTheme: IconTheme = DefaultIconTheme
    var applicationInfo: ApplicationInfo = ApplicationInfo.getInstance()
    var start: Instant? = null
    var file: VirtualFile? = null

    fun build(): ActivityFactory {
        val start = start ?: timeTrackingService.getOrTrack(project)
        return file?.let { FileActivityFactory(iconTheme, applicationInfo, start, project, it) }
            ?: ProjectActivityFactory(iconTheme, applicationInfo, start, project)
    }
}