package io.github.viniciusalvesmello.roomkotlincoroutinesflow.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainCoroutineDispatcher
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren

interface AppCoroutines {
    fun scope(): CoroutineScope
    fun dispatcherMain(): MainCoroutineDispatcher
    fun dispatcherDefault(): CoroutineDispatcher
    fun dispatcherIO(): CoroutineDispatcher
    fun cancel()
}

class AppCoroutinesImpl : AppCoroutines {

    private val job: Job = SupervisorJob()
    private val scope: CoroutineScope = CoroutineScope(dispatcherMain() + job)

    override fun scope(): CoroutineScope = scope

    override fun dispatcherMain(): MainCoroutineDispatcher = Dispatchers.Main

    override fun dispatcherDefault(): CoroutineDispatcher = Dispatchers.Default

    override fun dispatcherIO(): CoroutineDispatcher = Dispatchers.IO

    override fun cancel() {
        scope.coroutineContext.cancelChildren()
    }
}

class FakeAppCoroutinesImpl : AppCoroutines {

    private val job: Job = Job()
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Unconfined + job)

    override fun scope(): CoroutineScope = scope

    override fun dispatcherMain(): MainCoroutineDispatcher = Dispatchers.Main

    override fun dispatcherDefault(): CoroutineDispatcher = Dispatchers.Unconfined

    override fun dispatcherIO(): CoroutineDispatcher = Dispatchers.Unconfined

    override fun cancel() {
        scope.coroutineContext.cancelChildren()
    }
}