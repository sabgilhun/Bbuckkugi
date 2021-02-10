package com.sabgil.bbuckkugi.data.pref

import android.content.Context
import android.content.SharedPreferences
import com.sabgil.bbuckkugi.BuildConfig
import com.sabgil.bbuckkugi.data.model.enums.Gender
import com.sabgil.bbuckkugi.data.model.enums.LoginWay
import javax.inject.Inject

class AppSharedPreferenceImpl @Inject constructor(context: Context) : AppSharedPreference {

    private val pref: SharedPreferences = context.applicationContext
        .getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE)

    override var loginWay: LoginWay?
        get() = LoginWay.from(pref.getString(KEY_LOGIN_WAY, null))
        set(value) {
            value?.let { pref.edit().putString(KEY_LOGIN_WAY, it.desc).apply() }
        }

    override var id: String?
        get() = pref.getString(KEY_ID, null)
        set(value) {
            pref.edit().putString(KEY_ID, value).apply()
        }

    override var gender: Gender?
        get() = Gender.from(pref.getString(KEY_GENDER, null))
        set(value) {
            value?.let { pref.edit().putString(KEY_GENDER, it.desc).apply() }
        }

    override var name: String?
        get() = pref.getString(KEY_NICKNAME, null)
        set(value) {
            pref.edit().putString(KEY_NICKNAME, value).apply()
        }

    override var profileImageUrl: String?
        get() = pref.getString(PROFILE_IMAGE_URL, null)
        set(value) {
            pref.edit().putString(PROFILE_IMAGE_URL, value).apply()
        }

    companion object {
        private const val KEY_LOGIN_WAY = "KEY_LOGIN_WAY"
        private const val KEY_ID = "KEY_ID"
        private const val KEY_GENDER = "KEY_GENDER"
        private const val KEY_NICKNAME = "KEY_NICKNAME"
        private const val PROFILE_IMAGE_URL = "PROFILE_IMAGE_URL"
    }
}