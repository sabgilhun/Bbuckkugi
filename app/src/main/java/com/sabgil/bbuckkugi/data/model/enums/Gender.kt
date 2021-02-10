package com.sabgil.bbuckkugi.data.model.enums

enum class Gender(val desc: String) {
    MALE("남성"), FEMALE("여성");

    companion object {

        fun from(desc: String?): Gender? = desc?.let {
            when (it) {
                "남성" -> MALE
                "여성" -> FEMALE
                else -> null
            }
        }
    }
}