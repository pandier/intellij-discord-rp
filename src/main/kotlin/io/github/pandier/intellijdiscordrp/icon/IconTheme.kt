package io.github.pandier.intellijdiscordrp.icon

import com.intellij.openapi.application.ApplicationInfo
import com.intellij.openapi.fileTypes.FileType

interface IconTheme {

    fun getPlatform(info: ApplicationInfo): String

    fun getFile(type: FileType): String
}
