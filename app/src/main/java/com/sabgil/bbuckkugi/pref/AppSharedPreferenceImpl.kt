package com.sabgil.bbuckkugi.pref

import android.content.Context
import android.content.SharedPreferences
import com.sabgil.bbuckkugi.BuildConfig
import javax.inject.Inject

class AppSharedPreferenceImpl @Inject constructor(context: Context) : AppSharedPreference {

    private val pref: SharedPreferences = context.applicationContext
        .getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE)

    override var nickname: String?
        get() = pref.getString(KEY_NICKNAME, null)
        set(value) {
            pref.edit().putString(KEY_NICKNAME, value).apply()
        }

    companion object {
        private const val KEY_NICKNAME = "KEY_NICKNAME"
    }
}