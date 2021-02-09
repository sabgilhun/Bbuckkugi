package com.sabgil.bbuckkugi.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sabgil.bbuckkugi.common.Data
import com.sabgil.bbuckkugi.common.SingleLiveEvent
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
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

    protected fun showErrorMessage(throwable: Throwable) {
        _showErrorMessage.value = throwable.message ?: "일시적인 문제가 발생했습니다."
    }

    protected fun <T> Flow<Data<T>>.collectResult(
        context: CoroutineContext = ioErrorHandler,
        block: FlowResultScope<T>.() -> Unit
    ): Job {
        val flowResultScope = FlowResultScope<T>()
        flowResultScope.block()

        return collectOnMain(context) {
            when (it) {
                is Data.Success -> flowResultScope.onSuccess(it.data)
                is Data.Failure -> flowResultScope.onError(it.exception)
            }
        }
    }

    private fun <T> Flow<T>.collectOnMain(
        context: CoroutineContext,
        block: suspend CoroutineScope.(T) -> Unit
    ) = viewModelScope.launch(context) {
        collect {
            withContext(Dispatchers.Main) { block(it) }
        }
    }

    class FlowResultScope<T> {
        var onSuccess: suspend (T) -> Unit = {}
        var onError: suspend (Throwable) -> Unit = {}

        fun success(onSuccess: suspend (T) -> Unit) {
            this.onSuccess = onSuccess
        }

        fun error(onError: suspend (Throwable) -> Unit) {
            this.onError = onError
        }
    }
}