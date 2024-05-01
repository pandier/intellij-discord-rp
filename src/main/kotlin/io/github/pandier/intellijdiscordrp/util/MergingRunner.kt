package io.github.pandier.intellijdiscordrp.util

import kotlinx.coroutines.*
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * A runner that allows only one task to be running at a time and merges any new tasks
 * with the existing one. Tasks will be launched in a couroutine with the specified [scope] and [context].
 *
 * This class is thread-safe.
 *
 * @see [run]
 */
class MergingRunner<T>(
    private val scope: CoroutineScope,
    private val context: CoroutineContext = EmptyCoroutineContext,
) {
    private lateinit var deferred: Deferred<T>
    private val lock: Lock = ReentrantLock(true)

    /**
     * Runs the [task] or merges with an already running task.
     *
     * If the previous task already finished (or there was no previous task),
     * the given task will be launched in a new coroutine with the [scope] and [context] specified in the constructor
     * and the result will be returned. If the previous task didn't finish yet,
     * it will wait and return the result of that task.
     */
    fun run(task: suspend () -> T): Deferred<T> {
        return lock.withLock {
            if (!::deferred.isInitialized || deferred.isCompleted)
                deferred = CompletableDeferred<T>().also {
                    scope.launch(context) { it.completeWith(runCatching { task() }) }
                }
            deferred
        }
    }
}