package io.github.pandier.intellijdiscordrp.util.git

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import git4idea.GitUtil
import git4idea.repo.GitRepository

/**
 * An implementation of the [Git] facade using the Git4Idea plugin.
 * This should only be accessed if the Git4Idea plugin is actually installed,
 * because it accesses the API directly.
 *
 * If an instance of [Git] is needed, use [git] property instead.
 */
class GitImpl : Git {
    override fun getRepo(project: Project, file: VirtualFile?): GitRepo? {
        val repositoryManager = GitUtil.getRepositoryManager(project)
        val repository = file?.let { repositoryManager.getRepositoryForFileQuick(it) }
            ?: repositoryManager.repositories.firstOrNull()
            ?: return null
        return GitRepoImpl(repository)
    }
}

class GitRepoImpl(val repo: GitRepository) : GitRepo {
    override val remoteUrl: String?
        get() = GitUtil.getDefaultOrFirstRemote(repo.remotes)?.firstUrl
    override val branch: String?
        get() = repo.currentBranchName
}
