package com.example.itc__onl2_swd4_s3_1.ui.ui.loginSignupPages

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

open class AuthViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    init {
        checkAuthStatus()
    }

    fun checkAuthStatus() {
        _authState.value = if (auth.currentUser == null) {
            AuthState.Unauthenticated
        } else {
            AuthState.Authenticated
        }
    }

     fun login(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState.Error("Email or password can't be empty")
            return
        }
        _authState.value = AuthState.Loading
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            _authState.value = if (task.isSuccessful) {
                AuthState.Authenticated
            } else {
                AuthState.Error(task.exception?.message ?: "Something went wrong")
            }
        }
    }

    fun signup(firstName: String, lastName: String, email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState.Error("Email or password can't be empty")
            return
        }

        _authState.value = AuthState.Loading

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                val userData = hashMapOf(
                    "uid" to user?.uid,
                    "firstName" to firstName,
                    "lastName" to lastName,
                    "email" to email
                )

                // 🔧 Firestore instance (you need this!)
                val db = com.google.firebase.firestore.FirebaseFirestore.getInstance()

                user?.uid?.let { uid ->
                    db.collection("users").document(uid).set(userData)
                        .addOnSuccessListener {
                            _authState.value = AuthState.Authenticated
                        }
                        .addOnFailureListener { e ->
                            _authState.value = AuthState.Error("Failed to save user data: ${e.message}")
                        }
                }
            } else {
                _authState.value = AuthState.Error(task.exception?.message ?: "Something went wrong")
            }
        }
    }


    fun signout() {
        auth.signOut()
        _authState.value = AuthState.Unauthenticated
    }
}

// Auth State Sealed Class
sealed class AuthState {
    object Authenticated : AuthState()
    object Unauthenticated : AuthState()
    object Loading : AuthState()
    data class Error(val message: String) : AuthState()
}
