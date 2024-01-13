package com.example.restapi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.restapi.databinding.ActivityAddNoteBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.lang.ref.PhantomReference

class AddNoteActivity : AppCompatActivity() {

    private val binding : ActivityAddNoteBinding by lazy {
        ActivityAddNoteBinding.inflate(layoutInflater)
    }

    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // initilize firebase
        databaseReference = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()

        binding.saveBtn.setOnClickListener {

            val tittle = binding.title.text.toString()
            val description = binding.description.text.toString()

            if (tittle.isEmpty() && description.isEmpty()){
                Toast.makeText(this,"fill Both The Field",Toast.LENGTH_SHORT).show()

            }else{
                val currentUser:FirebaseUser? = auth.currentUser
                currentUser?.let { user ->
                    // genrate unique key
                    val noteKey: String? = databaseReference.child("users").child(user.uid).child("Notes").push().key

                    //note item instance
                    val noteItem = NoteItem(tittle,description,noteKey?:"")
                    if(noteKey != null){
                        databaseReference.child("users").child(user.uid).child("notes").child(noteKey).setValue(noteItem)
                            .addOnCompleteListener { task ->
                                if(task.isSuccessful){
                                    Toast.makeText(this,"Notes Svae Succesfull",Toast.LENGTH_SHORT).show()
                                    finish()
                                }else{
                                    Toast.makeText(this,"Failed",Toast.LENGTH_SHORT).show()
                                }
                            }
                    }
                }
            }
        }
    }
}