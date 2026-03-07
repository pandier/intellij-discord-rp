package io.github.pandier.intellijdiscordrp.activity

import com.intellij.openapi.application.ApplicationInfo
import com.intellij.openapi.application.ApplicationNamesInfo
import io.github.pandier.intellijdiscordrp.activity.icon.APPLICATION_ICON_REPOSITORY
import java.util.Locale

enum class ActivityApplicationType(
    val productCode: String?,
    iconFileName: String,
    val guessNames: List<String> = listOf(),
    hasClassicVariant: Boolean = false,
) {
    ANDROID_STUDIO(
        productCode = "AI",
        guessNames = listOf("android studio"),
        iconFileName = "androidstudio.png",
    ),
    APPCODE(
        productCode = "OC",
        guessNames = listOf("appcode"),
        iconFileName = "appcode.png",
    ),
    AQUA(
        productCode = "QA",
        guessNames = listOf("aqua"),
        iconFileName = "aqua.png",
        hasClassicVariant = true
    ),
    CLION(
        productCode = "CL",
        guessNames = listOf("clion"),
        iconFileName = "clion.png",
        hasClassicVariant = true
    ),
    DATAGRIP(
        productCode = "DB",
        guessNames = listOf("datagrip"),
        iconFileName = "datagrip.png",
        hasClassicVariant = true
    ),
    DATASPELL(
        productCode = "DS",
        guessNames = listOf("dataspell"),
        iconFileName = "dataspell.png",
        hasClassicVariant = true
    ),
    GOLAND(
        productCode = "GO",
        guessNames = listOf("goland"),
        iconFileName = "goland.png",
        hasClassicVariant = true
    ),
    INTELLIJ_IDEA_EDUCATIONAL(
        productCode = "IE",
        guessNames = listOf("intellij idea educational edition", "intellij idea educational"),
        iconFileName = "idea.png",
        hasClassicVariant = true
    ),
    INTELLIJ_IDEA_COMMUNITY(
        productCode = "IC",
        guessNames = listOf("intellij idea community edition", "intellij idea community", "intellij idea"),
        iconFileName = "idea.png",
        hasClassicVariant = true
    ),
    INTELLIJ_IDEA_ULTIMATE(
        productCode = "IU",
        guessNames = listOf("intellij idea ultimate edition", "intellij idea ultimate"),
        iconFileName = "idea.png",
        hasClassicVariant = true
    ),
    JETBRAINS_GATEWAY(
        productCode = "GW",
        guessNames = listOf("jetbrains gateway", "gateway"),
        iconFileName = "gateway.png",
    ),
    MPS(
        productCode = "MPS",
        guessNames = listOf("mps"),
        iconFileName = "mps.png",
    ),
    PHPSTORM(
        productCode = "PS",
        guessNames = listOf("phpstorm"),
        iconFileName = "phpstorm.png",
        hasClassicVariant = true
    ),
    PYCHARM_EDUCATIONAL(
        productCode = "PE",
        guessNames = listOf("pycharm educational edition", "pycharm educational"),
        iconFileName = "pycharm.png",
        hasClassicVariant = true
    ),
    PYCHARM_COMMUNITY(
        productCode = "PC",
        guessNames = listOf("pycharm community edition", "pycharm community", "pycharm"),
        iconFileName = "pycharm.png",
        hasClassicVariant = true
    ),
    PYCHARM_PROFESSIONAL(
        productCode = "PY",
        guessNames = listOf("pycharm professional edition", "pycharm professional"),
        iconFileName = "pycharm.png",
        hasClassicVariant = true
    ),
    RIDER(
        productCode = "RD",
        guessNames = listOf("rider"),
        iconFileName = "rider.png",
        hasClassicVariant = true
    ),
    RUBYMINE(
        productCode = "RM",
        guessNames = listOf("rubymine"),
        iconFileName = "rubymine.png",
        hasClassicVariant = true
    ),
    RUSTROVER(
        productCode = "RR",
        guessNames = listOf("rustrover"),
        iconFileName = "rustrover.png",
        hasClassicVariant = true
    ),
    WEBSTORM(
        productCode = "WS",
        guessNames = listOf("webstorm"),
        iconFileName = "webstorm.png",
        hasClassicVariant = true
    ),
    WRITERSIDE(
        productCode = "WRS",
        guessNames = listOf("writerside"),
        iconFileName = "writerside.png",
    ),
    OTHER(
        productCode = null,
        iconFileName = "fallback.png",
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
