package com.sabgil.bbuckkugi.common.ext

import android.app.Activity
import androidx.annotation.MainThread
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import com.sabgil.bbuckkugi.base.BaseActivity
import com.sabgil.bbuckkugi.base.BaseViewModel
import com.sabgil.bbuckkugi.common.CommonDialogFragment
import com.sabgil.bbuckkugi.common.DialogScope

@MainThread
inline fun <reified VM : BaseViewModel> BaseActivity<*>.viewModelOf() = getViewModelLazy(VM::class)

inline fun <reified T : BaseActivity<*>> Activity.startForResult(
    requestCode: Int,
    vararg pairs: Pair<String, Any?>
) = startActivityForResult(intentFor(T::class.java, *pairs), requestCode)

fun FragmentActivity.showPopupDialog(block: DialogScope.() -> Unit) =
    CommonDialogFragment.show(DialogScope().apply(block), supportFragmentManager)

fun AppCompatActivity.checkSelfPermissionCompat(permission: String) =
    ActivityCompat.checkSelfPermission(this, permission)

fun AppCompatActivity.requestPermissionsCompat(
    permissionsArray: Array<String>,
    requestCode: Int
) = ActivityCompat.requestPermissions(this, permissionsArray, requestCode)