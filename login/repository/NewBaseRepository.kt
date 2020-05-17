
import android.util.Log
import okio.IOException
import retrofit2.Response

open class NewBaseRepository {
    suspend fun <T : Any> safeApiCall(call: suspend () -> Response<T>, error: String): T? {
        return try {
            val result = newsApiOutput(call, error)
            var output: T? = null
            when (result) {
                is Output.Success -> output = result.output
                is Output.Error -> Log.e("Error", "The $error and the ${result.exception}")
            }
            return output
        } catch (e: java.io.IOException) {
            null
        }
    }

    private suspend fun <T : Any> newsApiOutput(call: suspend () -> Response<T>, error: String): Output<T> {
        val response = call.invoke()
        return if (response.isSuccessful)
            Output.Success(response.body()!!)
        else
            Output.Error(IOException("OOps .. Something went wrong due to  $error"))
    }
}
