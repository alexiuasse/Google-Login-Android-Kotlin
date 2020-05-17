package com.br.deliveryapp.ui.login.network

import com.br.deliveryapp.util.AppConstants.Companion.AUTH_GOOGLE
import com.br.deliveryapp.util.AppConstants.Companion.AUTH_GOOGLE_ACCESS_TOKEN
import com.br.deliveryapp.util.AppConstants.Companion.CHECK_STATUS
import com.br.deliveryapp.util.database.entity.auth.GoogleSignInAccessTokenDataClass
import com.br.deliveryapp.util.database.entity.auth.LoginGoogleEntity
import com.br.deliveryapp.util.database.entity.auth.LoginGoogleResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.*

interface LoginApi {

    @POST(AUTH_GOOGLE)
    @Headers("Content-Type: application/json")
    fun postLoginGoogleAsync(@Body data: LoginGoogleEntity): Deferred<Response<LoginGoogleResponse>>

    @POST(AUTH_GOOGLE_ACCESS_TOKEN)
    @FormUrlEncoded
    fun getAccessTokenAsync(
        @Field("grant_type") grant_type: String,
        @Field("client_id") client_id: String,
        @Field("client_secret") client_secret: String,
        @Field("redirect_uri") redirect_uri: String,
        @Field("code") authCode: String,
        @Field("id_token") id_token: String
    ): Deferred<Response<GoogleSignInAccessTokenDataClass>>

    @GET(CHECK_STATUS)
    fun checkStatusAsync(): Deferred<Response<Any>>

}