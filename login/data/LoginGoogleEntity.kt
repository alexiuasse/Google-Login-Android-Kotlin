
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginGoogleEntity (
    val access_token: String
)
