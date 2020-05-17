

class LoginRepository(private val apiInterface: LoginApi) : NewBaseRepository() {

    suspend fun postGoogleLogin(data: LoginGoogleEntity): LoginGoogleResponse? {
        return try {
            safeApiCall(
                call = { apiInterface.postLoginGoogleAsync(data).await() },
                error = "Erro ao enviar login ao servidor"
            )
        } catch (ex: Exception) {
            null
        }
    }

    suspend fun getAccessToken(
        grant_type: String,
        client_id: String,
        client_secret: String,
        redirect_uri: String,
        authCode: String,
        id_token: String
    ): GoogleSignInAccessTokenDataClass? {
        return try {
            safeApiCall(
                call = {
                    apiInterface.getAccessTokenAsync(
                        grant_type,
                        client_id,
                        client_secret,
                        redirect_uri,
                        authCode,
                        id_token
                    ).await()
                },
                error = "Erro ao pegar access token"
            ) as GoogleSignInAccessTokenDataClass
        } catch (ex: Exception) {
            null
        }
    }
    
}
