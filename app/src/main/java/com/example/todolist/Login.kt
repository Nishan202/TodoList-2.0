package com.example.todolist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.todolist.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Login : AppCompatActivity() {
    private var auth: FirebaseAuth = Firebase.auth
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.createAccount.setOnClickListener {
            val intent = Intent(this, Signup::class.java)
            startActivity(intent)
        }

        binding.forgotPassword.setOnClickListener {
            startActivity(Intent(this, ForgotPassword::class.java))
        }

        emailLogin()

    }

    private fun emailLogin() {
        binding.loginBtn.setOnClickListener { //when login button clicked.

            val email = binding.email.text.toString()
            val pass = binding.password.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty()){
                binding.progressBar.visibility = View.VISIBLE
                auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
                    if (it.isSuccessful){ //if the login successful, then change activity to main activity
                        val firebaseUser: FirebaseUser = it.result!!.user!!
                        val intent = Intent(this, MainActivity::class.java)
//                        intent.putExtra("EMAIL", firebaseUser.email)
                        Toast.makeText(this,"Login Successful", Toast.LENGTH_LONG).show()
                        binding.progressBar.visibility = View.GONE
                        startActivity(intent)
                        finish()
                    }else{
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(this,"Login Failed!", Toast.LENGTH_LONG).show()
                    }
                }
            }else{
                Toast.makeText(this, "Empty Fields Are no Allowed", Toast.LENGTH_LONG).show()
            }

        }
    }

    override fun onStart() { //if user already login, then can't go back to login activity
        super.onStart()
        if (auth.currentUser != null){
            Intent(this, MainActivity::class.java).also {
                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK //destination flag so as not to be able to use back
                startActivity(it)
            }
        }
    }
}