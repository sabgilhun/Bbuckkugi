package com.sabgil.bbuckkugi.api

import retrofit2.http.GET
import retrofit2.http.Header

interface NaverLoginApi {

    @GET(value = "me")
    suspend fun loadUserInfo(@Header("Authorization") header: String): NaverLoginUserInfoResponse
}