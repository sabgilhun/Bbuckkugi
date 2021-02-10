package com.sabgil.bbuckkugi.api

import com.google.gson.annotations.SerializedName

data class NaverLoginUserInfoResponse(
    @SerializedName("resultcode") val resultCode: String,
    @SerializedName("message") val message: String,
    @SerializedName("response") val response: Response?
) {

    data class Response(
        @SerializedName("id") val id: String,
        @SerializedName("name") val name: String?,
        @SerializedName("gender") val gender: String?,
        @SerializedName("profile_image") val profileImageUrl: String?
    )
}