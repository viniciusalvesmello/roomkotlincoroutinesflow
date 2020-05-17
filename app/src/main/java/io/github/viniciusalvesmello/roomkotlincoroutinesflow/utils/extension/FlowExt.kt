package io.github.viniciusalvesmello.roomkotlincoroutinesflow.utils.extension

import io.github.viniciusalvesmello.roomkotlincoroutinesflow.utils.ResourceResponse
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.utils.StateView
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.utils.buildResourceResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

fun <T> Flow<T>.asResourceResponse(scope: CoroutineScope): ResourceResponse<T> {
    val (result, state, error) = buildResourceResponse<T>()

    this.buffer()
        .onStart {
            state.postValue(StateView.LOADING)
        }.onEach { data ->
            result.postValue(data)
            state.postValue(StateView.SUCCESS)
        }.catch {
            state.postValue(StateView.ERROR)
            error.postValue(it)
        }.launchIn(scope)

    return ResourceResponse(
        data = result,
        state = state,
        error = error
    )
}