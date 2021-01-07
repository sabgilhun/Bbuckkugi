package com.sabgil.bbuckkugi.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sabgil.bbuckkugi.common.SingleLiveEvent
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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

    fun CoroutineScope.launchWithLoading(
        context: CoroutineContext = ioErrorHandler,
        block: suspend () -> Unit
    ) {
        launch {
            _isLoading.value = true
            block()
            _isLoading.value = false
        }
    }


}