package io.github.pandier.intellijdiscordrp.util.git

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import git4idea.GitUtil

/**
 * An implementation of the [Git] facade using the Git4Idea plugin.
 * This should only be accessed if the Git4Idea plugin is actually installed,
 * because it accesses the API directly.
 *
 * If an instance of [Git] is needed, use [git] property instead.
 */
class GitImpl : Git {
    override fun getRemote(project: Project, file: VirtualFile?): String? {
        val repositoryManager = GitUtil.getRepositoryManager(project)
        val repository = file?.let { repositoryManager.getRepositoryForFileQuick(it) }
            ?: repositoryManager.repositories.firstOrNull()
        return repository?.remotes?.let(GitUtil::getDefaultOrFirstRemote)?.firstUrl
    }
}