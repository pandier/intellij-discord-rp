package io.github.pandier.intellijdiscordrp.activity

import com.intellij.openapi.application.ApplicationInfo
import io.github.pandier.intellijdiscordrp.activity.icon.APPLICATION_ICON_REPOSITORY

enum class ActivityApplicationType(
    val productCodes: Set<String> = hashSetOf(),
    iconFileName: String = "fallback.png",
    val discordApplicationId: Long = 1221061074640703588L,
    hasClassicVariant: Boolean = false,
) {
    ANDROID_STUDIO(hashSetOf("AI"), "androidstudio.png", 1226140636021788702L),
    APPCODE(hashSetOf("OC"), "appcode.png", 1226140915278675988L),
    AQUA(hashSetOf("QA"), "aqua.png", 1226141064277262347L, hasClassicVariant = true),
    CLION(hashSetOf("CL"), "clion.png", 1221061311719280710L, hasClassicVariant = true),
    DATAGRIP(hashSetOf("DB"), "datagrip.png", 1226141195085021235L, hasClassicVariant = true),
    DATASPELL(hashSetOf("DS"), "dataspell.png", 1226141342997151764L, hasClassicVariant = true),
    GOLAND(hashSetOf("GO"), "goland.png", 1221061846522658837L, hasClassicVariant = true),
    INTELLIJ_IDEA(hashSetOf("IC", "IU", "IE"), "idea.png", 1107202385799041054L, hasClassicVariant = true),
    JETBRAINS_GATEWAY(hashSetOf("GW"), "gateway.png", 1226141930644307989L),
    MPS(hashSetOf("MPS"), "mps.png", 1226141482424205414L),
    PHPSTORM(hashSetOf("PS"), "phpstorm.png", 1221061217385189477L, hasClassicVariant = true),
    PYCHARM(hashSetOf("PC", "PY", "PE"), "pycharm.png", 1221060663066234961L, hasClassicVariant = true),
    RIDER(hashSetOf("RD"), "rider.png", 1221061953783336960L, hasClassicVariant = true),
    RUBYMINE(hashSetOf("RM"), "rubymine.png", 1226141638976606230L, hasClassicVariant = true),
    RUSTROVER(hashSetOf("RR"), "rustrover.png", 1226141797558779914L, hasClassicVariant = true),
    WEBSTORM(hashSetOf("WS"), "webstorm.png", 1221061703505281114L, hasClassicVariant = true),
    WRITERSIDE(hashSetOf("WRS"), "writerside.png", 1226142092590317688L),
    OTHER;

    val modernIcon: String = "$APPLICATION_ICON_REPOSITORY/$iconFileName"
    val classicIcon: String = if (hasClassicVariant) "$APPLICATION_ICON_REPOSITORY/classic/$iconFileName" else modernIcon
}

val currentActivityApplicationType: ActivityApplicationType by lazy {
    val productCode = ApplicationInfo.getInstance().build.productCode
    ActivityApplicationType.values().find { it.productCodes.contains(productCode) }
        ?: ActivityApplicationType.OTHER
}
