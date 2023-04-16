package com.example.todolist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class Account : AppCompatActivity() {
    private var auth: FirebaseAuth = Firebase.auth
    private var user = Firebase.auth.currentUser
    private val uid = user?.uid //get user id from database
    private var dbRef: DatabaseReference = FirebaseDatabase.getInstance().getReference(uid!!)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

//        var email: TextView = findViewById(R.id.tvEmail)
//        val mail = intent.getStringExtra("EMAIL")
//
//        email.text = "$mail"
//        Log.d("TAG", "found the email")
        accountDetails()
        logout()
    }

    private fun accountDetails(){

        val tvName: TextView = findViewById(R.id.tvName)
        val tvEmail: TextView = findViewById(R.id.tvEmail)
        val tvPicture: TextView = findViewById(R.id.picture)
        val verified: CardView = findViewById(R.id.verified)
        val notVerified: CardView = findViewById(R.id.notVerified)


        user?.reload() //reload user, so the verified badge can be change once the user have already verified the email.
        user?.let {
            // Name and email address
            val userName = user!!.displayName
            val email = user!!.email

            if (user!!.isEmailVerified){ //check if user email already verified
                verified.visibility = View.VISIBLE
                notVerified.visibility = View.GONE

                verified.setOnClickListener {
                    Toast.makeText(this@Account, "Your account is verified!", Toast.LENGTH_LONG).show()
                }
            }else{
                notVerified.visibility = View.VISIBLE
                verified.visibility = View.GONE

                notVerified.setOnClickListener {
                    user?.sendEmailVerification()?.addOnCompleteListener {
                        if (it.isSuccessful){
                            Toast.makeText(this@Account, "Check Your Email! (Including Spam)", Toast.LENGTH_LONG).show()
                        }else{
                            Toast.makeText(this@Account, "${it.exception?.message}", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }

            val splitValue = email?.split("@") //
            val name = splitValue?.get(0)
            tvName.text = name.toString()
            tvPicture.text = name?.get(0).toString().uppercase()
            tvEmail.text = email.toString()

//            if (userName != null) {
//                tvName.text = userName.toString()
//                tvPicture.text = userName[0].toString().uppercase()
//            }

        }
    }

    private fun logout() {
        val btnLogout: ImageButton = findViewById(R.id.btnLogout)
        btnLogout.setOnClickListener {
            auth.signOut()
            Intent(this, Login::class.java).also {
                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(it)
            }
        }
    }
}