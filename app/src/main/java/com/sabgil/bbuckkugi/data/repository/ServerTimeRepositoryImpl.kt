package com.sabgil.bbuckkugi.data.repository

import com.sabgil.bbuckkugi.data.api.ServerTimeApi

import javax.inject.Inject

class ServerTimeRepositoryImpl @Inject constructor(
    private val serverTimeApi: ServerTimeApi
) : ServerTimeRepository {

    override suspend fun getSeoulTime() = serverTimeApi.loadServerTime("Asia/Seoul").unixTime
}