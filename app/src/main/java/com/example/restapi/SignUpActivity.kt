package com.example.restapi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText

class SignUpActivity : AppCompatActivity() {
    private lateinit var databaseReference : DatabaseReference
    companion object{
        const val KEY1 = "com.example.registrationApp.SignInActivity.mail"
        const val KEY2 = "com.example.registrationApp.SignInActivity.name"

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val signin = findViewById<Button>(R.id.buttonSignup)
        val emailAdd = findViewById<TextInputEditText>(R.id.etMail)

        signin.setOnClickListener {
            //take ref till node user
            val usernameString = emailAdd.text.toString()
            if(usernameString.isNotBlank()) {
                readData(usernameString)
            }else{
                Toast.makeText(this,"Please Enter email address", Toast.LENGTH_SHORT).show()
            }
        }


    }// on create method over

    private fun readData(email: String) {
        databaseReference = FirebaseDatabase.getInstance() .getReference("Users")
        databaseReference.child(email).get().addOnSuccessListener {

            if(it.exists()){
                // welcome user
                val username = it.child("name").value
                val emailId = it.child("email").value
                val intentWelcome = Intent(this,welcomeActivity::class.java)
                intentWelcome.putExtra(KEY1,emailId.toString())
                intentWelcome.putExtra(KEY2,username.toString())
                startActivity(intentWelcome)

            }else{
                Toast.makeText(this,"user does not exist", Toast.LENGTH_SHORT).show()

            }


        }.addOnFailureListener{
            Toast.makeText(this,"Failed", Toast.LENGTH_SHORT).show()
        }
    }
}