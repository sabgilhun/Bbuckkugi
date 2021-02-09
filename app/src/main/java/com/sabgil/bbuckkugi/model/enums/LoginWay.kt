package com.sabgil.bbuckkugi.model.enums

enum class LoginWay(val desc: String) {
    KAKAO("카카오"), NAVER("네이버");

    companion object {

        fun from(desc: String?): LoginWay? = desc?.let {
            when (it) {
                "카카오" -> KAKAO
                "네이버" -> NAVER
                else -> null
            }
        }
    }
}