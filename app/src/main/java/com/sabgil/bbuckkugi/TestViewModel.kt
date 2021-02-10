package com.sabgil.bbuckkugi

import androidx.hilt.lifecycle.ViewModelInject
import com.sabgil.bbuckkugi.base.BaseViewModel
import com.sabgil.bbuckkugi.data.repository.ConnectionManager


class TestViewModel @ViewModelInject constructor(
    val connectionManager: ConnectionManager
) : BaseViewModel() {

    fun test() {
        print("")
    }
}