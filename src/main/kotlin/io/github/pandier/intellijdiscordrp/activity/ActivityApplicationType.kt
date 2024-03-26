package io.github.pandier.intellijdiscordrp.activity

import com.intellij.openapi.application.ApplicationInfo
import io.github.pandier.intellijdiscordrp.activity.icon.APPLICATION_ICON_REPOSITORY

enum class ActivityApplicationType(
    val productCodes: List<String> = listOf(),
    private val iconFile: String = "fallback.png",
    val discordApplicationId: Long = 1221061074640703588L,
) {
    INTELLIJ_IDEA(listOf("IC", "IU", "IE"), "ij.png", 1107202385799041054L),
    PYCHARM(listOf("PC", "PY", "PE"), "pc.png", 1221060663066234961L),
    PHPSTORM(listOf("PS"), "ps.png", 1221061217385189477L),
    WEBSTORM(listOf("WS"), "ws.png", 1221061703505281114L),
    CLION(listOf("CL"), "cl.png", 1221061311719280710L),
    GOLAND(listOf("GO"), "go.png", 1221061846522658837L),
    RIDER(listOf("RD"), "rd.png", 1221061953783336960L),
    OTHER;

    val icon: String
        get() = "$APPLICATION_ICON_REPOSITORY/$iconFile"
}

val currentActivityApplicationType: ActivityApplicationType by lazy {
    val productCode = ApplicationInfo.getInstance().build.productCode
    ActivityApplicationType.values().find { it.productCodes.contains(productCode) }
        ?: ActivityApplicationType.OTHER
}
