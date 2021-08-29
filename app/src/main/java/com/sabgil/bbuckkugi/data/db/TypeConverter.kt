package com.sabgil.bbuckkugi.data.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sabgil.bbuckkugi.data.model.enums.Gender
import com.sabgil.bbuckkugi.data.model.enums.LoginWay
import java.lang.reflect.Type

class TypeConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromLoginWay(loginWay: LoginWay): String =
        gson.toJson(loginWay)

    @TypeConverter
    fun toLoginWay(json: String): LoginWay =
        gson.fromJson(json, loginWayTypeToken)

    @TypeConverter
    fun fromGender(gender: Gender): String =
        gson.toJson(gender)

    @TypeConverter
    fun toGender(json: String): Gender =
        gson.fromJson(json, genderTypeToken)

    companion object {
        private val loginWayTypeToken: Type = object : TypeToken<LoginWay>() {}.type
        private val genderTypeToken: Type = object : TypeToken<Gender>() {}.type
    }
}