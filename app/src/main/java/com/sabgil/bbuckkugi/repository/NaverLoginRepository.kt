package com.sabgil.bbuckkugi.repository

import com.sabgil.bbuckkugi.model.UserInfo

interface NaverLoginRepository {

    suspend fun login()

    suspend fun loadUserInfo(): UserInfo
}