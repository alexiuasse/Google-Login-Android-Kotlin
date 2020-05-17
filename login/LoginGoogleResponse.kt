package com.br.deliveryapp.util.database.entity.login

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginGoogleResponse(
    val key: String
)