package com.sabgil.bbuckkugi.presentation.ui.namesetting

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.sabgil.bbuckkugi.R
import com.sabgil.bbuckkugi.base.BaseActivity
import com.sabgil.bbuckkugi.common.ext.viewModelOf
import com.sabgil.bbuckkugi.databinding.ActivityNameSettingBinding
import com.sabgil.bbuckkugi.presentation.ui.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NameSettingActivity :
    BaseActivity<ActivityNameSettingBinding>(R.layout.activity_name_setting) {

    private val viewModel by viewModelOf<NameSettingViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.viewModel = viewModel

        viewModel.nicknameStoreSuccess.observe {
            HomeActivity.startOnTop(this)
        }
    }

    companion object {

        fun startOnTop(context: Context) {
            val intent = Intent(context, NameSettingActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            context.startActivity(intent)
        }
    }
}