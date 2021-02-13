package com.sabgil.bbuckkugi.presentation.ui.home

import androidx.hilt.lifecycle.ViewModelInject
import com.sabgil.bbuckkugi.base.BaseViewModel
import com.sabgil.bbuckkugi.data.repository.ConnectionManager

class HomeViewModel @ViewModelInject constructor(
    private val connectionManager: ConnectionManager
) : BaseViewModel() {

}