package com.sabgil.bbuckkugi.data.api

import com.google.gson.annotations.SerializedName

data class ServerTimeResponse(
    @SerializedName("unixtime") val unixTime: Long
)