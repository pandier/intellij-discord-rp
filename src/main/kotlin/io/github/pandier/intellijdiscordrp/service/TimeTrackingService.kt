package io.github.pandier.intellijdiscordrp.service

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import java.time.Instant

@Service(Service.Level.PROJECT)
class TimeTrackingService {
    companion object {
        @JvmStatic
        fun getInstance(project: Project): TimeTrackingService = project.service()
    }

    val start: Instant = Instant.now()
}
