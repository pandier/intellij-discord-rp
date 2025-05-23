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
private const val ICON_REPOSITORY_COMMIT = "2cf74d990e3efc5a84f2741ab1f3b29b0056a719"
const val ICON_REPOSITORY =
    "https://raw.githubusercontent.com/pandier/intellij-discord-rp/$ICON_REPOSITORY_COMMIT/icons"
const val FILE_ICON_REPOSITORY = "$ICON_REPOSITORY/file"
const val APPLICATION_ICON_REPOSITORY = "$ICON_REPOSITORY/application"
