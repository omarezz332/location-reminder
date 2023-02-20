package com.udacity.project4.authentication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.observe
import com.firebase.ui.auth.AuthUI
import com.udacity.project4.R
import com.udacity.project4.databinding.ActivityAuthenticationBinding
import com.udacity.project4.locationreminders.RemindersActivity


import com.firebase.ui.auth.AuthMethodPickerLayout

/*
 * This class should be the starting point of the app, It asks the users to sign in / register, and redirects the
 * signed in users to the RemindersActivity.
 */
class AuthenticationActivity : AppCompatActivity() {

    // Get a reference to the ViewModel scoped to this Fragment.
    private lateinit var launcher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityAuthenticationBinding>(this, R.layout.activity_authentication)
        binding.signInButton.setOnClickListener { signInFlow() }

        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { check ->
            if (check.resultCode == RESULT_OK) {
                Log.d("TAG", "Login Successful")
                startActivity(Intent(this, RemindersActivity::class.java))
                finish()
            }
        }

//        viewModel.state.observe(this) { authentication ->
//            when (authentication) {
//                MyLoginViewModel.State.AUTHENTICATED -> {
//                    startActivity(Intent(this, RemindersActivity::class.java))
//                }else -> Log.d("TAG", "Failed to login")
//            }
//        }
    }

    private fun signInFlow() {
        val providers = arrayListOf(AuthUI.IdpConfig.EmailBuilder().build(), AuthUI.IdpConfig.GoogleBuilder().build())

        launcher.launch(AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(providers).build())
    }
}

