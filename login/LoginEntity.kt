package com.br.deliveryapp.util.database.entity.login

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginEntity(
    val username: String,
    val password: String
//    var registration_id: String
)