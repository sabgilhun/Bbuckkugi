package com.sabgil.bbuckkugi

import androidx.hilt.lifecycle.ViewModelInject
import com.sabgil.bbuckkugi.base.BaseViewModel


class TestViewModel @ViewModelInject constructor() : BaseViewModel() {

    fun test() {
        print("")
    }
}