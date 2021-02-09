package com.sabgil.bbuckkugi.ui.login

import android.content.Context
import android.os.Bundle
import com.sabgil.bbuckkugi.R
import com.sabgil.bbuckkugi.base.BaseActivity
import com.sabgil.bbuckkugi.common.ext.startOnTop
import com.sabgil.bbuckkugi.common.ext.viewModelOf
import com.sabgil.bbuckkugi.databinding.ActivityLoginBinding
import com.sabgil.bbuckkugi.ui.home.HomeActivity
import com.sabgil.bbuckkugi.ui.namesetting.NameSettingActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding>(R.layout.activity_login) {

    private val viewModel by viewModelOf<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.viewModel = viewModel

        viewModel.needNicknameSetting.observeNonNull {
            if (it) {
                NameSettingActivity.startOnTop(this)
            } else {
                HomeActivity.startOnTop(this)
            }
        }
    }

    companion object {

        fun startOnTop(context: Context) = context.startOnTop<LoginActivity>()
    }
}