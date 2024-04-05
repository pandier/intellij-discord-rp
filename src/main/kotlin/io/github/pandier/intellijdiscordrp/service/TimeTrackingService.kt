package io.github.pandier.intellijdiscordrp.service

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import java.time.Instant

@Service
class TimeTrackingService {
    companion object {
        @JvmStatic
        fun getInstance(): TimeTrackingService = service()
    }

    private val times: MutableMap<String, Instant> = mutableMapOf()

    fun track(
        project: Project,
        time: Instant = Instant.now()
    ): Instant {
        times[project.name] = time
        return time
    }

    fun stop(project: Project) {
        times.remove(project.name)
    }

    fun get(project: Project): Instant? =
        times[project.name]

    fun getOrTrack(project: Project): Instant =
        get(project) ?: track(project, Instant.now())
}
