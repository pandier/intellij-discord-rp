package io.github.pandier.intellijdiscordrp.service

import com.intellij.openapi.application.Application
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Key
import com.intellij.openapi.util.UserDataHolder
import com.intellij.openapi.vfs.VirtualFile
import java.time.Instant

@Service
class TimeTrackingService {
    companion object {
        @JvmStatic
        val TIME_START_KEY = Key.create<Instant>("discordrp.time.start")

        @JvmStatic
        fun getInstance(): TimeTrackingService = service()
    }

    fun getOrInit(application: Application): Instant =
        getOrInitInternal(application)

    fun getOrInit(project: Project): Instant =
        getOrInitInternal(project)

    fun getOrInit(file: VirtualFile): Instant =
        getOrInitInternal(file)

    private fun getOrInitInternal(dataHolder: UserDataHolder): Instant {
        return dataHolder.getUserData(TIME_START_KEY) ?: Instant.now().also {
            dataHolder.putUserData(TIME_START_KEY, it)
        }
    }
}
