package com.sabgil.bbuckkugi.presentation.ui.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sabgil.bbuckkugi.base.BaseViewModel
import com.sabgil.bbuckkugi.data.model.Message
import com.sabgil.bbuckkugi.data.repository.MessageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel @ViewModelInject constructor(
    private val messageRepository: MessageRepository
) : BaseViewModel() {

    private val _items = MutableLiveData<List<Message.Reply>>()
    val items: LiveData<List<Message.Reply>> get() = _items

    fun loadMessages() {
        viewModelScope.launch {
            val messages = withContext(Dispatchers.IO) {
                messageRepository.selectAllMessage().take(3)
            }
            _items.value = messages
        }
    }
}