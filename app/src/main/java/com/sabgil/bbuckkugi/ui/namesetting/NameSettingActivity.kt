package com.sabgil.bbuckkugi.ui.namesetting

import android.os.Bundle
import com.sabgil.bbuckkugi.R
import com.sabgil.bbuckkugi.base.BaseActivity
import com.sabgil.bbuckkugi.common.ext.viewModelOf
import com.sabgil.bbuckkugi.databinding.ActivityNameSettingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NameSettingActivity :
    BaseActivity<ActivityNameSettingBinding>(R.layout.activity_name_setting) {

    private val viewModel by viewModelOf<NameSettingViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}