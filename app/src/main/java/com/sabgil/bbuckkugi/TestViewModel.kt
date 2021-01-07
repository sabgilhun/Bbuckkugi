package com.sabgil.bbuckkugi

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel


class TestViewModel @ViewModelInject constructor(): ViewModel() {

    fun test() {
        print("")
    }
}