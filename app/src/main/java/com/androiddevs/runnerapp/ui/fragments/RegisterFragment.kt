package com.androiddevs.runnerapp.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.androiddevs.runnerapp.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.android.synthetic.main.fragment_register.btnRegister
import kotlinx.android.synthetic.main.fragment_register.etName
import kotlinx.android.synthetic.main.fragment_register.etPassword
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class RegisterFragment : Fragment(R.layout.fragment_register) {
    lateinit var auth: FirebaseAuth

    private var registerSuccess = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        btnRegister.setOnClickListener {
            registerUser()
            if(checkLoggedInState()) {
                findNavController().navigate(R.id.action_registerFragment_to_setupFragment)
            }
        }

    }

    private fun registerUser() {
        val email = etName.text.toString()
        val password = etPassword.text.toString()
        if (email.isNotEmpty() && password.isNotEmpty()) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    auth.createUserWithEmailAndPassword(email, password).await()
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