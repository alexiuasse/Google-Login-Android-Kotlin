package com.br.deliveryapp.util.database.entity.login

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginGoogleEntity (
    val access_token: String
)