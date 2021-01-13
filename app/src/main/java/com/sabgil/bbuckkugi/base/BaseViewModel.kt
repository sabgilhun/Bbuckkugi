package com.sabgil.bbuckkugi.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sabgil.bbuckkugi.common.Result
import com.sabgil.bbuckkugi.common.SingleLiveEvent
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel : ViewModel() {

    private val errorHandler = CoroutineExceptionHandler { _, _ ->
        _showErrorMessage.setValue("일시적인 문제가 발생했습니다.")
    }

    private val ioErrorHandler = errorHandler + Dispatchers.IO

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _showErrorMessage = SingleLiveEvent<String>()
    val showErrorMessage: LiveData<String> = _showErrorMessage

    fun ioScopeLaunch(
        context: CoroutineContext = ioErrorHandler,
        block: suspend CoroutineScope.() -> Unit
    ) = viewModelScope.launch(context = context, block = block)

    protected fun <T> Flow<Result<T>>.collectResult(block: FlowResultScope<T>.() -> Unit) {
        val flowResultScope = FlowResultScope(this, viewModelScope + ioErrorHandler, _isLoading)
        flowResultScope.block()
        flowResultScope.internalLaunch()
    }

    class FlowResultScope<T>(
        private val flow: Flow<Result<T>>,
        private val scope: CoroutineScope,
        private val loading: MutableLiveData<Boolean>
    ) {
        private var onSuccess: suspend (T) -> Unit = {}
        private var onError: suspend (Throwable) -> Unit = {}

        fun success(onSuccess: suspend (T) -> Unit) {
            this.onSuccess = onSuccess
        }

        fun error(onError: suspend (Throwable) -> Unit) {
            this.onError = onError
        }

        fun internalLaunch() {
            scope.launch {
                flow.collect {
                    withContext(Dispatchers.Main) {
                        when (it) {
                            is Result.Success -> {
                                loading.value = false
                                onSuccess(it.result)
                            }
                            is Result.Failure -> {
                                loading.value = false
                                onError(it.exception)
                            }
                            Result.Loading -> {
                                loading.value = true
                            }
                        }
                    }
                }
            }
        }
    }
}