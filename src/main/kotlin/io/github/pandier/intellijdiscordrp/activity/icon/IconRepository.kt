package io.github.pandier.intellijdiscordrp.activity.icon

/*
 * HOW TO UPDATE ICON REPOSITORY
 *
 * The icon repository uses a specific git commit hash to avoid incompability
 * between plugin versions. If an icon is added, modified or removed,
 * commit these changes. Then copy the hash of the commit and paste it
 * into ICON_REPOSITORY_COMMIT constant. Commit this new change as:
 *
 * chore: Update icon repository
 *
 * When these changes make it into the repository, the icons
 * will start displaying correctly.
 */
private const val ICON_REPOSITORY_COMMIT = "bf29ce96ea6b2d6a1af8a1c064b9fd95be5f7011"
const val ICON_REPOSITORY =
    "https://raw.githubusercontent.com/pandier/intellij-discord-rp/$ICON_REPOSITORY_COMMIT/icons"
const val FILE_ICON_REPOSITORY = "$ICON_REPOSITORY/file"
const val APPLICATION_ICON_REPOSITORY = "$ICON_REPOSITORY/application"
