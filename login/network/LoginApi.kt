import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.*

interface LoginApi {
    
    @POST(URL_TO_YOUR_BACKEND)
    fun postLoginGoogleAsync(@Body data: LoginGoogleEntity): Deferred<Response<LoginGoogleResponse>>

    @POST("https://www.googleapis.com/oauth2/v4/token")
    @FormUrlEncoded
    fun getAccessTokenAsync(
        @Field("grant_type") grant_type: String,
        @Field("client_id") client_id: String,
        @Field("client_secret") client_secret: String,
        @Field("redirect_uri") redirect_uri: String,
        @Field("code") authCode: String,
        @Field("id_token") id_token: String
    ): Deferred<Response<GoogleSignInAccessTokenDataClass>>

}
