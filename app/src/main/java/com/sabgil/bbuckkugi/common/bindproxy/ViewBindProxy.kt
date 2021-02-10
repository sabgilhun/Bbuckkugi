package com.sabgil.bbuckkugi.common.bindproxy

import android.view.View
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter

@set:BindingAdapter("isVisible")
var View.isVisibleProxy: Boolean
    get() = isVisible
    set(value) {
        isVisible = value
    }