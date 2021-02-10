package com.sabgil.bbuckkugi.presentation.ui.login

import android.content.Context
import android.os.Bundle
import com.sabgil.bbuckkugi.R
import com.sabgil.bbuckkugi.base.BaseActivity
import com.sabgil.bbuckkugi.common.ext.startOnTop
import com.sabgil.bbuckkugi.common.ext.viewModelOf
import com.sabgil.bbuckkugi.databinding.ActivityLoginBinding
import com.sabgil.bbuckkugi.presentation.ui.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding>(R.layout.activity_login) {

    private val viewModel by viewModelOf<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.viewModel = viewModel

        viewModel.doneSaveUserInfo.observe {
            HomeActivity.startOnTop(this)
        }
    }

    override fun onDestroy() {
        viewModelStore.clear()
        super.onDestroy()
    }

    companion object {

        fun startOnTop(context: Context) = context.startOnTop<LoginActivity>()
    }
}