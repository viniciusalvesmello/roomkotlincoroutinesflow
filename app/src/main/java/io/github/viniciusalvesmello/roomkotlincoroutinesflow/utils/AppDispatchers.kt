package io.github.viniciusalvesmello.roomkotlincoroutinesflow.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainCoroutineDispatcher

interface AppDispatchers {
    fun dispatcherMain(): MainCoroutineDispatcher
    fun dispatcherDefault(): CoroutineDispatcher
    fun dispatcherIO(): CoroutineDispatcher
}

class AppDispatchersImpl: AppDispatchers {

    override fun dispatcherMain(): MainCoroutineDispatcher = Dispatchers.Main

    override fun dispatcherDefault(): CoroutineDispatcher = Dispatchers.Default

    override fun dispatcherIO(): CoroutineDispatcher = Dispatchers.IO
}

class FakeAppDispatchersImpl: AppDispatchers {

    override fun dispatcherMain(): MainCoroutineDispatcher = Dispatchers.Main

    override fun dispatcherDefault(): CoroutineDispatcher = Dispatchers.Unconfined

    override fun dispatcherIO(): CoroutineDispatcher = Dispatchers.Unconfined
}