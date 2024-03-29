package com.sabgil.bbuckkugi.data.api

import retrofit2.http.GET
import retrofit2.http.Header

interface NaverLoginApi {

    @GET(value = "me")
    suspend fun loadUserInfo(@Header("Authorization") header: String): NaverLoginUserInfoResponse
}