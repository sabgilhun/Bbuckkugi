package com.sabgil.bbuckkugi.ui.login

import android.content.Context
import android.os.Bundle
import com.kakao.sdk.auth.LoginClient
import com.kakao.sdk.user.UserApiClient
import com.sabgil.bbuckkugi.R
import com.sabgil.bbuckkugi.base.BaseActivity
import com.sabgil.bbuckkugi.common.ext.startOnTop
import com.sabgil.bbuckkugi.common.ext.viewModelOf
import com.sabgil.bbuckkugi.databinding.ActivityLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding>(R.layout.activity_login) {

    private val viewModel by viewModelOf<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.viewModel = viewModel

        binding.loginWithKakao.setOnClickListener {
            viewModel.loginWithKakao()
        }
    }

    companion object {

        fun startOnTop(context: Context) = context.startOnTop<LoginActivity>()
    }
}