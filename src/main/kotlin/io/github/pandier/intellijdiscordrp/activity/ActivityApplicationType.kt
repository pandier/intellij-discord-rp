package io.github.pandier.intellijdiscordrp.activity

import com.intellij.openapi.application.ApplicationInfo
import io.github.pandier.intellijdiscordrp.activity.icon.APPLICATION_ICON_REPOSITORY

enum class ActivityApplicationType(
    val productCodes: List<String> = listOf(),
    private val iconFile: String = "fallback.png",
) {
    INTELLIJ_IDEA(listOf("IC", "IU", "IE"), "ij.png"),
    PYCHARM(listOf("PC", "PY", "PE"), "pc.png"),
    WEBSTORM(listOf("WS"), "ws.png"),
    CLION(listOf("CL"), "cl.png"),
    GOLAND(listOf("GO"), "go.png"),
    RIDER(listOf("RD"), "rd.png"),
    OTHER;

    val icon: String
        get() = "$APPLICATION_ICON_REPOSITORY/$iconFile"
}

val currentActivityApplicationType: ActivityApplicationType by lazy {
    val productCode = ApplicationInfo.getInstance().build.productCode
    ActivityApplicationType.values().find { it.productCodes.contains(productCode) }
        ?: ActivityApplicationType.OTHER
}
