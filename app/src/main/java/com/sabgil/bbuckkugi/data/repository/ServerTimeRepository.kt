package com.sabgil.bbuckkugi.data.repository

interface ServerTimeRepository {

    suspend fun getSeoulTime(): Long
}