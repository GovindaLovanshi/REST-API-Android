package com.example.restapi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText

class LoginActivity : AppCompatActivity() {
    // databaseReference is a class

    private lateinit var dataBase : DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val signUpbtn  =  findViewById<Button>(R.id.buttonSignup)
        val etName = findViewById<TextInputEditText>(R.id.etName)
        val etMail = findViewById<TextInputEditText>(R.id.etMail)
        val etPassword = findViewById<TextInputEditText>(R.id.etPassword)

        signUpbtn.setOnClickListener {

            // all value collect

            val name = etName.text.toString()
            val mail = etMail.text.toString()
            val passWord = etPassword.text.toString()

            val user = User(name, passWord ,mail)// object

            // path tk pahuch
            dataBase = FirebaseDatabase.getInstance().getReference("Users")
            dataBase.child(name).setValue(user).addOnSuccessListener {

                etName.text?.clear()
                etMail.text?.clear()
                etPassword.text?.clear()


                Toast.makeText(this,"User Registered", Toast.LENGTH_SHORT).show()
            }.addOnSuccessListener {
                Toast.makeText(this,"Failed", Toast.LENGTH_SHORT).show()
            }
        }

        val signInText = findViewById<TextView>(R.id.tvSignIn)
        signInText.setOnClickListener {
            val openSignInActivity = Intent(this,SignInActivity::class.java)
            startActivity(openSignInActivity)
        }

    }
}