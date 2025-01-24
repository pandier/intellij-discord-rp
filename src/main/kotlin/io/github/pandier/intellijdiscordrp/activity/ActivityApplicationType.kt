package io.github.pandier.intellijdiscordrp.activity

import com.intellij.openapi.application.ApplicationInfo
import com.intellij.openapi.application.ApplicationNamesInfo
import io.github.pandier.intellijdiscordrp.activity.icon.APPLICATION_ICON_REPOSITORY
import java.util.Locale

enum class ActivityApplicationType(
    val productCode: String?,
    iconFileName: String,
    val discordApplicationId: Long,
    val guessNames: List<String> = listOf(),
    val fullNameDiscordApplicationId: Long? = null,
    hasClassicVariant: Boolean = false,
) {
    ANDROID_STUDIO(
        productCode = "AI",
        guessNames = listOf("android studio"),
        iconFileName = "androidstudio.png",
        discordApplicationId = 1226140636021788702L
    ),
    APPCODE(
        productCode = "OC",
        guessNames = listOf("appcode"),
        iconFileName = "appcode.png",
        discordApplicationId = 1226140915278675988L
    ),
    AQUA(
        productCode = "QA",
        guessNames = listOf("aqua"),
        iconFileName = "aqua.png",
        discordApplicationId = 1226141064277262347L,
        hasClassicVariant = true
    ),
    CLION(
        productCode = "CL",
        guessNames = listOf("clion"),
        iconFileName = "clion.png",
        discordApplicationId = 1221061311719280710L,
        hasClassicVariant = true
    ),
    DATAGRIP(
        productCode = "DB",
        guessNames = listOf("datagrip"),
        iconFileName = "datagrip.png",
        discordApplicationId = 1226141195085021235L,
        hasClassicVariant = true
    ),
    DATASPELL(
        productCode = "DS",
        guessNames = listOf("dataspell"),
        iconFileName = "dataspell.png",
        discordApplicationId = 1226141342997151764L,
        hasClassicVariant = true
    ),
    GOLAND(
        productCode = "GO",
        guessNames = listOf("goland"),
        iconFileName = "goland.png",
        discordApplicationId = 1221061846522658837L,
        hasClassicVariant = true
    ),
    INTELLIJ_IDEA_EDUCATIONAL(
        productCode = "IE",
        guessNames = listOf("intellij idea educational edition", "intellij idea educational"),
        iconFileName = "idea.png",
        discordApplicationId = 1107202385799041054L,
        hasClassicVariant = true
    ),
    INTELLIJ_IDEA_COMMUNITY(
        productCode = "IC",
        guessNames = listOf("intellij idea community edition", "intellij idea community", "intellij idea"),
        iconFileName = "idea.png",
        discordApplicationId = 1107202385799041054L,
        fullNameDiscordApplicationId = 1311226032862531604L,
        hasClassicVariant = true
    ),
    INTELLIJ_IDEA_ULTIMATE(
        productCode = "IU",
        guessNames = listOf("intellij idea ultimate edition", "intellij idea ultimate"),
        iconFileName = "idea.png",
        discordApplicationId = 1107202385799041054L,
        fullNameDiscordApplicationId = 1311226821496279143L,
        hasClassicVariant = true
    ),
    JETBRAINS_GATEWAY(
        productCode = "GW",
        guessNames = listOf("jetbrains gateway", "gateway"),
        iconFileName = "gateway.png",
        discordApplicationId = 1226141930644307989L
    ),
    MPS(
        productCode = "MPS",
        guessNames = listOf("mps"),
        iconFileName = "mps.png",
        discordApplicationId = 1226141482424205414L
    ),
    PHPSTORM(
        productCode = "PS",
        guessNames = listOf("phpstorm"),
        iconFileName = "phpstorm.png",
        discordApplicationId = 1221061217385189477L,
        hasClassicVariant = true
    ),
    PYCHARM_EDUCATIONAL(
        productCode = "PE",
        guessNames = listOf("pycharm educational edition", "pycharm educational"),
        iconFileName = "pycharm.png",
        discordApplicationId = 1221060663066234961L,
        hasClassicVariant = true
    ),
    PYCHARM_COMMUNITY(
        productCode = "PC",
        guessNames = listOf("pycharm community edition", "pycharm community", "pycharm"),
        iconFileName = "pycharm.png",
        discordApplicationId = 1221060663066234961L,
        fullNameDiscordApplicationId = 1311226996084445237L,
        hasClassicVariant = true
    ),
    PYCHARM_PROFESSIONAL(
        productCode = "PY",
        guessNames = listOf("pycharm professional edition", "pycharm professional"),
        iconFileName = "pycharm.png",
        discordApplicationId = 1221060663066234961L,
        fullNameDiscordApplicationId = 1311227148991725628L,
        hasClassicVariant = true
    ),
    RIDER(
        productCode = "RD",
        guessNames = listOf("rider"),
        iconFileName = "rider.png",
        discordApplicationId = 1221061953783336960L,
        hasClassicVariant = true
    ),
    RUBYMINE(
        productCode = "RM",
        guessNames = listOf("rubymine"),
        iconFileName = "rubymine.png",
        discordApplicationId = 1226141638976606230L,
        hasClassicVariant = true
    ),
    RUSTROVER(
        productCode = "RR",
        guessNames = listOf("rustrover"),
        iconFileName = "rustrover.png",
        discordApplicationId = 1226141797558779914L,
        hasClassicVariant = true
    ),
    WEBSTORM(
        productCode = "WS",
        guessNames = listOf("webstorm"),
        iconFileName = "webstorm.png",
        discordApplicationId = 1221061703505281114L,
        hasClassicVariant = true
    ),
    WRITERSIDE(
        productCode = "WRS",
        guessNames = listOf("writerside"),
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

/**
 * Guesses the application type based on the application names.
 */
private fun guessApplicationType(): ActivityApplicationType? {
    val appNames = ApplicationNamesInfo.getInstance()
    val name1 = appNames.fullProductName.lowercase(Locale.ROOT)
    val name2 = appNames.fullProductNameWithEdition.lowercase(Locale.ROOT)
    return ActivityApplicationType.values().find { it.guessNames.contains(name1) || it.guessNames.contains(name2) }
}

val currentActivityApplicationType: ActivityApplicationType by lazy {
    val productCode = ApplicationInfo.getInstance().build.productCode
    ActivityApplicationType.values().find { it.productCode == productCode }
        ?: guessApplicationType()
        ?: ActivityApplicationType.OTHER
}
