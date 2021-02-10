package com.sabgil.bbuckkugi.ui.login

import android.content.Context
import android.os.Bundle
import com.nhn.android.naverlogin.OAuthLogin
import com.nhn.android.naverlogin.OAuthLoginHandler
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

        binding.loginWithNaver.setOnClickListener {
            loginWithNaver()
        }
    }

    override fun onDestroy() {
        viewModelStore.clear()
        super.onDestroy()
    }

    private fun loginWithNaver() {
        val mOAuthLoginInstance = OAuthLogin.getInstance()

        OAuthLogin.getInstance().startOauthLoginActivity(this, object : OAuthLoginHandler() {
            override fun run(success: Boolean) {
                mOAuthLoginInstance.getState(this@LoginActivity)
                val accessToken = mOAuthLoginInstance.getAccessToken(this@LoginActivity)
                val refreshToken = mOAuthLoginInstance.getRefreshToken(this@LoginActivity)
                val tokenType = mOAuthLoginInstance.getTokenType(this@LoginActivity)

                Timber.i("$accessToken, $refreshToken, $tokenType")
            }
        })
    }

    companion object {

        fun startOnTop(context: Context) = context.startOnTop<LoginActivity>()
    }
}