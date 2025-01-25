package io.github.pandier.intellijdiscordrp.util.git

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile

/**
 * A facade for accessing Git functionality.
 */
interface Git {
    fun getRemote(project: Project, file: VirtualFile?): String?
}

/**
 * Holds the [Git] facade for the current IDE environment or null if Git is not available.
 */
val git: Git? by lazy {
    try {
        Class.forName("git4idea.GitUtil")
        return@lazy GitImpl()
    } catch (_: ClassNotFoundException) {
        return@lazy null
    }
}
