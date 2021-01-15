package com.sabgil.bbuckkugi.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import com.sabgil.bbuckkugi.R
import com.sabgil.bbuckkugi.common.ext.showPopupDialog
import kotlin.reflect.KClass

abstract class BaseActivity<B : ViewDataBinding>(
    @LayoutRes private val layoutId: Int
) : AppCompatActivity() {

    protected val binding: B by lazy { DataBindingUtil.setContentView(this, layoutId) }

    private val loadingDialog: AlertDialog by lazy {
        AlertDialog.Builder(this, R.style.LoadingDialog)
            .setCancelable(false)
            .setView(R.layout.widget_progress_bar)
            .create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.lifecycleOwner = this
    }

    fun <VM : BaseViewModel> getViewModelLazy(viewModelClass: KClass<VM>): Lazy<VM> =
        ViewModelHolder(viewModelClass, { viewModelStore }, { defaultViewModelProviderFactory })

    protected fun <V> LiveData<V>.observe(onChange: (V) -> Unit) {
        this.observe(this@BaseActivity, onChange)
    }

    protected fun <V> LiveData<V>.observeNonNull(onChange: (V) -> Unit) {
        this.observe(this@BaseActivity, { value ->
            if (value != null) {
                onChange(value)
            }
        })
    }

    private fun observingBaseViewModel(viewModel: BaseViewModel) {
        viewModel.isLoading.observe { if (it) loadingDialog.show() else loadingDialog.hide() }
        viewModel.showErrorMessage.observe {showErrorMessage(it)}
    }

    protected fun showErrorMessage(message: String) {
        showPopupDialog {
            title = "일시적인 에러가 발생했습니다."
            content = message
            isCancelable = false
            isVisibleCancelButton = false
        }
    }

    private inner class ViewModelHolder<VM : BaseViewModel>(
        private val viewModelClass: KClass<VM>,
        private val storeProducer: () -> ViewModelStore,
        private val factoryProducer: () -> ViewModelProvider.Factory
    ) : Lazy<VM> {
        private var _cached: VM? = null

        override val value: VM
            get() {
                val cached = _cached
                val viewModel = if (cached == null) {
                    val factory = factoryProducer()
                    val store = storeProducer()
                    ViewModelProvider(store, factory).get(viewModelClass.java).also {
                        _cached = it
                    }
                } else {
                    cached
                }

                return viewModel.apply { observingBaseViewModel(this) }
            }

        override fun isInitialized() = _cached != null
    }
}