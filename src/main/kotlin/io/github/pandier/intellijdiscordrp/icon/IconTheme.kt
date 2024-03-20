package io.github.pandier.intellijdiscordrp.icon

import com.intellij.openapi.application.ApplicationInfo
import com.intellij.openapi.fileTypes.FileType
import io.github.pandier.intellijdiscordrp.activity.ActivityInfo

interface IconTheme {

    fun getByType(
        type: IconType,
        activity: ActivityInfo,
    ) = getByType(type, activity.app, activity.file?.fileType)

    fun getByType(
        type: IconType,
        app: ApplicationInfo?,
        file: FileType?,
    ): String? = when (type) {
        IconType.APPLICATION -> app?.let { getPlatform(it) }
        IconType.FILE -> file?.let { getFile(it) }
    }

    fun getPlatform(info: ApplicationInfo): String

    fun getFile(type: FileType): String
}
