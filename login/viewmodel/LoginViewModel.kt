
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val repository = LoginRepository(ApiFactory.loginApi)
    val loginLiveData = MutableLiveData<LoginGoogleResponse>()
    val accessTokenLiveData = MutableLiveData<GoogleSignInAccessTokenDataClass>()
    val loading = MutableLiveData(GONE)

    fun postGoogleLogin(data: LoginGoogleEntity) = viewModelScope.launch {
        loading.postValue(VISIBLE)
        loginLiveData.postValue(repository.postGoogleLogin(data))
        loading.postValue(GONE)
    }

    fun getAcessToken(
        grant_type: String,
        client_id: String,
        client_secret: String,
        redirect_uri: String,
        authCode: String,
        id_token: String
    ) = viewModelScope.launch {
        loading.postValue(VISIBLE)
        accessTokenLiveData.postValue(
            repository.getAccessToken(
                grant_type,
                client_id,
                client_secret,
                redirect_uri,
                authCode,
                id_token
            )
        )
        loading.postValue(GONE)
    }

}
