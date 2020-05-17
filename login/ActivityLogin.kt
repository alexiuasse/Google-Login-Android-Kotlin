package com.br.deliveryapp.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.br.deliveryapp.App
import com.br.deliveryapp.R
import com.br.deliveryapp.ui.login.viewmodel.LoginViewModel
import com.br.deliveryapp.ui.main.ActivityMain
import com.br.deliveryapp.util.data.PreferenceRepository
import com.br.deliveryapp.util.database.entity.auth.LoginGoogleEntity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar


class ActivityLogin : AppCompatActivity() {

    companion object {
        const val RC_SIGN_IN = 1
        const val TAG = "ActivityLogin"
    }

    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var singInGoogle: Button
    private lateinit var layout: CoordinatorLayout
    private lateinit var status: ImageView
    private lateinit var loading: ProgressBar
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var preferenceRepository: PreferenceRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        preferenceRepository = (application as App).preferenceRepository
        singInGoogle = findViewById(R.id.sign_in_button)
        layout = findViewById(R.id.layout_activityLogin)
        status = findViewById(R.id.imageView_contentLogin_statusServidor)
        loading = findViewById(R.id.progressBar_activityLogin)
        loginViewModel = ViewModelProvider(this@ActivityLogin).get(LoginViewModel::class.java)

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.google_login_api))
            .requestServerAuthCode(getString(R.string.google_login_api))
            .requestEmail()
            .build()
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        preferenceRepository.nightModeLive.observe(this, Observer { nightMode ->
            nightMode?.let { delegate.localNightMode = it }
        })

        singInGoogle.setOnClickListener { signIn() }

        loginViewModel.loginLiveData.observe(this, Observer { loginResponse ->
            loginResponse?.let { login(it.key) }
            if (loginResponse == null) {
//                status.setImageResource(R.drawable.ic_thumb_down_f44336_24dp)
                GoogleSignIn.getClient(this, gso).signOut()
                Snackbar.make(layout, getString(R.string.erro_null), Snackbar.LENGTH_LONG)
                    .setAction(getString(R.string.geral_fechar)) { }
                    .show()
            }
        })

        loginViewModel.accessTokenLiveData.observe(this@ActivityLogin, Observer {
            Log.i(TAG, "ACCESSTOKEN: $it")
            if (it == null) {
//                status.setImageResource(R.drawable.ic_thumb_down_f44336_24dp)
                GoogleSignIn.getClient(this, gso).signOut()
                Snackbar.make(layout, getString(R.string.erro_null), Snackbar.LENGTH_LONG)
                    .setAction(getString(R.string.geral_fechar)) { }
                    .show()
            } else loginViewModel.postGoogleLogin(LoginGoogleEntity(access_token = it.access_token))
        })

        loginViewModel.status.observe(this@ActivityLogin, Observer {
            if (it == null) status.setImageResource(R.drawable.ic_thumb_down_f44336_24dp)
        })
        loginViewModel.loading.observe(this@ActivityLogin, Observer {
            loading.visibility = it
        })

    }

    private fun login(key: String) {
        preferenceRepository.tokenApi = key
        Intent(this@ActivityLogin, ActivityMain::class.java).apply {
            startActivity(this)
            finish()
        }
    }

    private fun signIn() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
//            Log.d(TAG, "${account?.email}")
            // Signed in successfully, show authenticated UI.
            updateUI(account)
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            updateUI(null)
        }
    }

    /**
     * Passar account.idToken para v1/auth/google/ em access_token o retorno é o token a ser utilizado
     */
    private fun updateUI(account: GoogleSignInAccount?) {
//        Log.i(TAG, "Sending idToken to server ACCOUNT EMAIL: ${account?.email}")
//        account?.idToken?.let { LoginGoogleEntity(access_token = it) }?.let { loginViewModel.postGoogleLogin(it) }
        Log.i(TAG, "ACCOUNT AUTH CODE: ${account?.serverAuthCode} ACCOUNT EMAIL: ${account?.email}")
        if (account?.serverAuthCode == null) signIn()
        account?.serverAuthCode?.let {
            account.idToken?.let { it1 ->
                Log.d(TAG, "MAKING CALL TO GET ACCESS TOKEN")
                loginViewModel.getAcessToken(
                    grant_type = "authorization_code",
                    client_id = "227003809683-0rg47j4gb295f6qt1sl8di0m2d7n8q5o.apps.googleusercontent.com",
                    client_secret = "IUclAzUtfKu0f_kEq21-ar8o",
                    authCode = it,
                    id_token = it1,
                    redirect_uri = ""
                )
            }
        }
    }

    override fun onStart() {
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        val account = GoogleSignIn.getLastSignedInAccount(this)
        loginViewModel.checkStatus()
        if (account != null) updateUI(account)
        // log in direct
        super.onStart()
    }
}
