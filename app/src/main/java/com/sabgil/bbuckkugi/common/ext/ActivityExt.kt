package com.sabgil.bbuckkugi.common.ext

import androidx.annotation.MainThread
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.sabgil.bbuckkugi.base.BaseActivity
import com.sabgil.bbuckkugi.base.BaseViewModel

@MainThread
inline fun <reified VM : BaseViewModel> BaseActivity<*>.viewModelOf() = getViewModelLazy(VM::class)