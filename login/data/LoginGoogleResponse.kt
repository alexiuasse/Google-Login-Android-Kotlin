package com.br.deliveryapp.util.database.entity.login

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginGoogleResponse(
    // i use django as backend and token authentication, this is the token
    val token: String
)
