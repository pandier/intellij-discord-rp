package io.github.pandier.intellijdiscordrp.activity

import com.intellij.openapi.application.ApplicationInfo
import io.github.pandier.intellijdiscordrp.activity.icon.APPLICATION_ICON_REPOSITORY

enum class ActivityApplicationType(
    val productCode: String?,
    iconFileName: String,
    val discordApplicationId: Long,
    val fullNameDiscordApplicationId: Long? = null,
    hasClassicVariant: Boolean = false,
) {
    ANDROID_STUDIO(
        productCode = "AI",
        iconFileName = "androidstudio.png",
        discordApplicationId = 1226140636021788702L
    ),
    APPCODE(
        productCode = "OC",
        iconFileName = "appcode.png",
        discordApplicationId = 1226140915278675988L),
    AQUA(
        productCode = "QA",
        iconFileName = "aqua.png",
        discordApplicationId = 1226141064277262347L,
        hasClassicVariant = true
    ),
    CLION(
        productCode = "CL",
        iconFileName = "clion.png",
        discordApplicationId = 1221061311719280710L,
        hasClassicVariant = true
    ),
    DATAGRIP(
        productCode = "DB",
        iconFileName = "datagrip.png",
        discordApplicationId = 1226141195085021235L,
        hasClassicVariant = true
    ),
    DATASPELL(
        productCode = "DS",
        iconFileName = "dataspell.png",
        discordApplicationId = 1226141342997151764L,
        hasClassicVariant = true
    ),
    GOLAND(
        productCode = "GO",
        iconFileName = "goland.png",
        discordApplicationId = 1221061846522658837L,
        hasClassicVariant = true
    ),
    INTELLIJ_IDEA_EDUCATIONAL(
        productCode = "IE",
        iconFileName = "idea.png",
        discordApplicationId = 1107202385799041054L,
        hasClassicVariant = true
    ),
    INTELLIJ_IDEA_COMMUNITY(
        productCode = "IC",
        iconFileName = "idea.png",
        discordApplicationId = 1107202385799041054L,
        fullNameDiscordApplicationId = 1311226032862531604L,
        hasClassicVariant = true
    ),
    INTELLIJ_IDEA_ULTIMATE(
        productCode = "IU",
        iconFileName = "idea.png",
        discordApplicationId = 1107202385799041054L,
        fullNameDiscordApplicationId = 1311226821496279143L,
        hasClassicVariant = true
    ),
    JETBRAINS_GATEWAY(
        productCode = "GW",
        iconFileName = "gateway.png",
        discordApplicationId = 1226141930644307989L
    ),
    MPS(
        productCode = "MPS",
        iconFileName = "mps.png",
        discordApplicationId = 1226141482424205414L
    ),
    PHPSTORM(
        productCode = "PS",
        iconFileName = "phpstorm.png",
        discordApplicationId = 1221061217385189477L,
        hasClassicVariant = true
    ),
    PYCHARM_EDUCATIONAL(
        productCode = "PE",
        iconFileName = "pycharm.png",
        discordApplicationId = 1221060663066234961L,
        hasClassicVariant = true
    ),
    PYCHARM_COMMUNITY(
        productCode = "PC",
        iconFileName = "pycharm.png",
        discordApplicationId = 1221060663066234961L,
        fullNameDiscordApplicationId = 1311226996084445237L,
        hasClassicVariant = true
    ),
    PYCHARM_PROFESSIONAL(
        productCode = "PY",
        iconFileName = "pycharm.png",
        discordApplicationId = 1221060663066234961L,
        fullNameDiscordApplicationId = 1311227148991725628L,
        hasClassicVariant = true
    ),
    RIDER(
        productCode = "RD",
        iconFileName = "rider.png",
        discordApplicationId = 1221061953783336960L,
        hasClassicVariant = true
    ),
    RUBYMINE(
        productCode = "RM",
        iconFileName = "rubymine.png",
        discordApplicationId = 1226141638976606230L,
        hasClassicVariant = true
    ),
    RUSTROVER(
        productCode = "RR",
        iconFileName = "rustrover.png",
        discordApplicationId = 1226141797558779914L,
        hasClassicVariant = true
    ),
    WEBSTORM(
        productCode = "WS",
        iconFileName = "webstorm.png",
        discordApplicationId = 1221061703505281114L,
        hasClassicVariant = true
    ),
    WRITERSIDE(
        productCode = "WRS",
        iconFileName = "writerside.png",
        discordApplicationId = 1226142092590317688L
    ),
    OTHER(
        productCode = null,
        iconFileName = "fallback.png",
        discordApplicationId = 1221061074640703588L
    );

    val modernIcon: String = "$APPLICATION_ICON_REPOSITORY/$iconFileName"
    val classicIcon: String = if (hasClassicVariant) "$APPLICATION_ICON_REPOSITORY/classic/$iconFileName" else modernIcon
}

val currentActivityApplicationType: ActivityApplicationType by lazy {
    val productCode = ApplicationInfo.getInstance().build.productCode
    ActivityApplicationType.values().find { it.productCode == productCode }
        ?: ActivityApplicationType.OTHER
}
