package com.sabgil.bbuckkugi.data.api

import retrofit2.http.GET
import retrofit2.http.Path

interface ServerTimeApi {

    @GET(value = "api/timezone/{timezone}")
    suspend fun loadServerTime(@Path("timezone") timeZone: String): ServerTimeResponse
}