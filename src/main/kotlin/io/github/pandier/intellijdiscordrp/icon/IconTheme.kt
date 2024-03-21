package io.github.pandier.intellijdiscordrp.icon

import io.github.pandier.intellijdiscordrp.activity.ActivityInfo

interface IconTheme {

    fun getByType(
        type: IconType,
        info: ActivityInfo,
    ) = getByType(type, info.appProductCode, info.file?.type)

    fun getByType(
        type: IconType,
        appProductCode: String?,
        fileType: String?,
    ): String? = when (type) {
        IconType.APPLICATION -> appProductCode?.let { getPlatform(it) }
        IconType.FILE -> fileType?.let { getFile(it) }
    }

    fun getPlatform(productCode: String): String

    fun getFile(type: String): String
}
