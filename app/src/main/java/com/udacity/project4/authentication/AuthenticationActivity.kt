package com.udacity.project4.authentication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.IdpResponse
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.udacity.project4.R
import com.udacity.project4.databinding.ActivityAuthenticationBinding
import com.udacity.project4.databinding.ActivityReminderDescriptionBindingImpl
import com.udacity.project4.locationreminders.RemindersActivity

/**
 * This class should be the starting point of the app, It asks the users to sign in / register, and redirects the
 * signed in users to the RemindersActivity.
 */
class AuthenticationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthenticationBinding
    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res ->
        this.onSignInResult(res)
    }
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_authentication
        )

        binding.lifecycleOwner = this

        binding.signInButton.setOnClickListener {
            launchSignIn()
        }


    }

    private fun launchSignIn() {
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build())

        // Create and launch sign-in intent
        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build()

        signInLauncher.launch(signInIntent)

    }
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if (requestCode == SIGN_IN_RESULT_CODE) {
//            val response = IdpResponse.fromResultIntent(data)
//            if (resultCode == Activity.RESULT_OK) {
//                // User successfully signed in
//                Log.i(TAG, "Successfully signed in user ${FirebaseAuth.getInstance().currentUser?.displayName}!")
//                Toast.makeText(this, "Successfully signed in", Toast.LENGTH_SHORT).show()
//                //startReminderActivity()
//                val intent = Intent(this, RemindersActivity::class.java)
//                startActivity(intent)
//                finish()
//
//            } else {
//                // Sign in failed. If response is null the user canceled the
//                // sign-in flow using the back button. Otherwise check
//                // response.getError().getErrorCode() and handle the error.
//                Log.i(TAG, "Sign in unsuccessful ${response?.error?.errorCode}")
//                Toast.makeText(this, "Sign in unsuccessful", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }


    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse
        if (result.resultCode == RESULT_OK) {
            // Successfully signed in
            val user = FirebaseAuth.getInstance().currentUser
            Log.i(TAG, "Successfully signed in user ${user?.displayName}!")
            Toast.makeText(this, "Successfully signed in", Toast.LENGTH_SHORT).show()
            startReminderActivity()
        } else {
            // Sign in failed. If response is null the user canceled the
            // sign-in flow using the back button. Otherwise check
            // response.getError().getErrorCode() and handle the error.
            // ...
            Log.i(TAG, "Sign in unsuccessful ${response?.error?.errorCode}")
            Toast.makeText(this, "Sign in unsuccessful", Toast.LENGTH_SHORT).show()
        }
    }

    private fun startReminderActivity() {
                        val intent = Intent(this, RemindersActivity::class.java)
                startActivity(intent)
                finish()
    }

    companion object {
        const val TAG = "AuthenticationActivity"
    }
}
