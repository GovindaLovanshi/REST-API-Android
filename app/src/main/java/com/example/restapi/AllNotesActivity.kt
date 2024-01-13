package com.example.restapi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.restapi.databinding.ActivityAddNoteBinding
import com.example.restapi.databinding.DiaologUpdateNoteBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AllNotesActivity : AppCompatActivity() ,NoteAdapter.OnItemClickListener{
    private val binding:ActivityAddNoteBinding by lazy{
        ActivityAddNoteBinding.inflate(layoutInflater)
    }

    private lateinit var databaseReference: DatabaseReference
    private  lateinit var auth:FirebaseAuth
    private lateinit var recyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        recyclerView = binding.noteRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)

        // initilize firebase database refernce

        databaseReference = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()

        val currentUser:FirebaseUser? = auth.currentUser
        currentUser.let { user->
            val noteReference:DatabaseReference = databaseReference.child("users").child(user.uid).child("notes")
            noteReference.addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                   val noteList = mutableListOf<NoteItem>()
                   for(noteSnapshot in snapshot.children){
                     val note:NoteItem? = noteSnapshot.getValue(NoteItem::class.java)
                       note?.let {
                           noteList.add(it)
                       }
                   }
                    noteList.reverse()
                    // add data
                    val adapter = NoteAdapter(noteList,this@AllNotesActivity)
                    recyclerView.adapter = adapter
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
        }
    }

    override fun onDeleteClick(noteId: String) {
        val currentUser:FirebaseUser? = auth.currentUser
        currentUser?.let { user->
            val noteReference:DatabaseReference = databaseReference.child("users").child(user.uid).child("notes")
            noteReference.child(noteId).removeValue()
        }

    }

    private fun updateNoteDataBase(noteId: String,newTitle: String,newDescription: String){
        val currentUser:FirebaseUser? = auth.currentUser
        currentUser?.let { user->
            val noteReference = databaseReference.child("users").child(user.uid).child("notes")
            val updateNote = NoteItem(newTitle,newDescription,noteId)
            noteReference.child(noteId).setValue(updateNote)
                .addOnCompleteListener{task->
                    if(task.isSuccessful){
                        Toast.makeText(this,"Note Updated Successful",Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(this," failed top Update Note",Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    override fun onUpdateClick(noteId: String,currentTitle:String,currentDescription:String) {

        val dialogBinding:DiaologUpdateNoteBinding =  DiaologUpdateNoteBinding.inflate(
            LayoutInflater.from(this))

        val dialog = AlertDialog.Builder(this).setView(dialogBinding.root)
            .setTitle("Update Notes")
            .setPositiveButton("Update"){dialog->
                val newTitle = dialogBinding.updateNoteTittle.text.toString()
                val newDescription  = dialogBinding.updateNoteDescription.text.toString()
                updateNoteDataBase(noteId,newTitle,newDescription)
                dialog.dismiss()

            }

            .setNegativeButton("cancel"){dialog->
                dialog.dismiss()
            }

            .create()
        dialogBinding.updateNoteTittle.setText(currentTitle)
        dialogBinding.updateNoteDescription.setText(currentDescription)

        dialog.show()


    }
}