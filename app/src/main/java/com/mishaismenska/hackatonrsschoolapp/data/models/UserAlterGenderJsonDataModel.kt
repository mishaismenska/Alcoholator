package com.mishaismenska.hackatonrsschoolapp.data.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserAlterGenderJsonDataModel(
    val GoogleUserIdToken: String,
    val UserGender: Int
)
