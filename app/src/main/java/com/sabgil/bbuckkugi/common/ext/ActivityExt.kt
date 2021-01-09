package com.sabgil.bbuckkugi.common.ext

import androidx.annotation.MainThread
import androidx.fragment.app.FragmentActivity
import com.sabgil.bbuckkugi.base.BaseActivity
import com.sabgil.bbuckkugi.base.BaseViewModel
import com.sabgil.bbuckkugi.common.CommonDialogFragment
import com.sabgil.bbuckkugi.common.DialogScope

@MainThread
inline fun <reified VM : BaseViewModel> BaseActivity<*>.viewModelOf() = getViewModelLazy(VM::class)

fun FragmentActivity.showPopupDialog(block: DialogScope.() -> Unit) =
    CommonDialogFragment.show(DialogScope().apply(block), supportFragmentManager)