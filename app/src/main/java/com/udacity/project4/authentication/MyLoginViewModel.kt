package com.udacity.project4.authentication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.map

class MyLoginViewModel : ViewModel() {

    enum class State {
        AUTHENTICATED, UNAUTHENTICATED, INVALID_AUTHENTICATION
    }

    val state = FirebaseUserLiveData().map { user ->
        if (user != null) {
            State.AUTHENTICATED
        } else {
            State.UNAUTHENTICATED
        }
    }
}