package io.github.viniciusalvesmello.roomkotlincoroutinesflow.utils.extension

import io.github.viniciusalvesmello.roomkotlincoroutinesflow.utils.AppCoroutines
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.utils.ResourceResponse
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.utils.StateView
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.utils.buildResourceResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

fun <T> Flow<T>.asCacheResourceResponse(
    appCoroutines: AppCoroutines,
    retry: (() -> ResourceResponse<T>)? = null,
    transformer: (T) -> T = { it },
    isEmptyPredicate: (T) -> Boolean = { false },
    sendEmptyData: Boolean = true
): ResourceResponse<T> {
    val (result, state, error) = buildResourceResponse<T>()

    appCoroutines.launchIO {
        this.onStart {
            state.postValue(StateView.LOADING)
        }.onEach { data ->
            val isEmpty = isEmptyPredicate(data)
            if (sendEmptyData || isEmpty.not()) result.postValue(transformer(data))
            val networkState = if (isEmpty) StateView.EMPTY else StateView.SUCCESS
            state.postValue(networkState)
        }.catch {
            state.postValue(StateView.ERROR)
            error.postValue(it)
        }.collect()
    }

    return ResourceResponse(
        data = result,
        state = state,
        error = error,
        retry = retry
    )
}