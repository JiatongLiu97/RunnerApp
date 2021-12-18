package com.androiddevs.runnerapp.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.androiddevs.runnerapp.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class LoginFragment : Fragment(R.layout.fragment_login) {

    lateinit var auth: FirebaseAuth
    private var isNotFirstAppOpen = false


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        isNotFirstAppOpen = checkLoggedInState()

        if(isNotFirstAppOpen) {
            val navOptions = NavOptions.Builder()
                .setPopUpTo(R.id.loginFragment, true)
                .build()
            findNavController().navigate(
                R.id.action_loginFragment_to_runFragment,
                savedInstanceState,
                navOptions
            )
        }

        btnLogin.setOnClickListener {
            loginUser()
            if (checkLoggedInState()) {
                findNavController().navigate(R.id.action_loginFragment_to_runFragment)
            }
        }

        btnRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }




    private fun loginUser() {
        val email = etName.text.toString()
        val password = etPassword.text.toString()
        if (email.isNotEmpty() && password.isNotEmpty()) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    auth.signInWithEmailAndPassword(email, password).await()
                    withContext(Dispatchers.Main) {


                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireContext(), e.message, Toast.LENGTH_LONG).show()

                    }
                }
            }
        }
    }

    private fun checkLoggedInState(): Boolean {
        return auth.currentUser != null
    }



}