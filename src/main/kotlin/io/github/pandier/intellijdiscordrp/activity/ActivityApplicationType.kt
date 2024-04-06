package io.github.pandier.intellijdiscordrp.activity

import com.intellij.openapi.application.ApplicationInfo
import io.github.pandier.intellijdiscordrp.activity.icon.APPLICATION_ICON_REPOSITORY

enum class ActivityApplicationType(
    val productCodes: List<String> = listOf(),
    private val iconFile: String = "fallback.png",
    val discordApplicationId: Long = 1221061074640703588L,
) {
    ANDROID_STUDIO(listOf("AI"), "androidstudio.png", 1226140636021788702L),
    APPCODE(listOf("OC"), "appcode.png", 1226140915278675988L),
    AQUA(listOf("QA"), "aqua.png", 1226141064277262347L),
    CLION(listOf("CL"), "clion.png", 1221061311719280710L),
    DATAGRIP(listOf("DB"), "datagrip.png", 1226141195085021235L),
    DATASPELL(listOf("DS"), "dataspell.png", 1226141342997151764L),
    GOLAND(listOf("GO"), "goland.png", 1221061846522658837L),
    INTELLIJ_IDEA(listOf("IC", "IU", "IE"), "idea.png", 1107202385799041054L),
    JETBRAINS_GATEWAY(listOf("GW"), "gateway.png", 1226141930644307989L),
    MPS(listOf("MPS"), "mps.png", 1226141482424205414L),
    PHPSTORM(listOf("PS"), "phpstorm.png", 1221061217385189477L),
    PYCHARM(listOf("PC", "PY", "PE"), "pycharm.png", 1221060663066234961L),
    RIDER(listOf("RD"), "rider.png", 1221061953783336960L),
    RUBYMINE(listOf("RM"), "rubymine.png", 1226141638976606230L),
    RUSTROVER(listOf("RR"), "rustrover.png", 1226141797558779914L),
    WEBSTORM(listOf("WS"), "webstorm.png", 1221061703505281114L),
    WRITERSIDE(listOf("WRS"), "writerside.png", 1226142092590317688L),
    OTHER;

    val icon: String
        get() = "$APPLICATION_ICON_REPOSITORY/$iconFile"
}

val currentActivityApplicationType: ActivityApplicationType by lazy {
    val productCode = ApplicationInfo.getInstance().build.productCode
    ActivityApplicationType.values().find { it.productCodes.contains(productCode) }
        ?: ActivityApplicationType.OTHER
}
