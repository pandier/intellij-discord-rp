package io.github.pandier.intellijdiscordrp.util

import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex

/**
 * A runner that allows only one task to be running at a time and merges
 * any new tasks with the existing one.
 *
 * This class is thread-safe.
 *
 * @see [run]
 */
class MergingRunner<T> {
    private lateinit var deferred: Deferred<T>
    private val mutex: Mutex = Mutex()

    /**
     * Runs the [task] or merges with an already running task.
     *
     * If the previous task already finished (or there was no previous task), the given task
     * will be launched in the current thread and the result will be returned. If the previous
     * task didn't finish yet, it will wait and return the result of that task.
     */
    suspend fun run(task: suspend () -> T): Deferred<T> {
        mutex.lock()
        return if (!::deferred.isInitialized || deferred.isCompleted) {
            CompletableDeferred<T>().also {
                deferred = it
                mutex.unlock()
                it.completeWith(runCatching { task() })
            }
        } else {
            deferred.also { mutex.unlock() }
        }
    }
}