package com.sabgil.bbuckkugi.ui.guide

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import com.sabgil.bbuckkugi.base.BaseViewModel

class GuideViewModel @ViewModelInject constructor() : BaseViewModel() {

    private val confirmedPageList = mutableSetOf<Int>()

    val canGoNext = MutableLiveData(false)

    fun changePage(position: Int) {
        confirmedPageList.add(position)
        canGoNext.value = confirmedPageList.size >= 4
    }
}