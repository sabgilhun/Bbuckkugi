package com.sabgil.bbuckkugi.data.repository

import com.sabgil.bbuckkugi.data.model.UserInfo

interface NaverLoginRepository {

    suspend fun login()

    suspend fun loadUserInfo(): UserInfo
}