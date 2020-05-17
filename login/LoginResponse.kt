package com.br.deliveryapp.util.database.entity.login

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginResponse(
    val non_field_errors: List<String>? = null,
    val key: String? = null
)