
object ApiFactory {

    val loginApi: LoginApi = RetrofitFactory.retrofit("YOUR_BASE_URL").create(LoginApi::class.java)

}
