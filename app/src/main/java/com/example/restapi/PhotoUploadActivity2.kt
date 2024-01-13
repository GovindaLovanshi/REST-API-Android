package com.example.restapi

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.restapi.databinding.ActivityPhotoUpload2Binding
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class PhotoUploadActivity2 : AppCompatActivity() {
    private lateinit var binding :ActivityPhotoUpload2Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding= ActivityPhotoUpload2Binding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.imageBtn.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_PICK
            intent.type = "image/*"
            imageLauncher.launch(intent)
        }


    }

    val imageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode== Activity.RESULT_OK ){
            if(it.data!=null){
                val ref = Firebase.storage.reference.child("Photo"+System.currentTimeMillis()+"."+getFiletype(it.data!!.data))
                ref.putFile(it.data!!.data!!).addOnSuccessListener {
                    ref.downloadUrl.addOnSuccessListener {
                        Firebase.database.reference.child("Photo").push().setValue(it.toString())

                        binding.image.setImageURI(it)
                        Toast.makeText(this@PhotoUploadActivity2,"Photo Uploaded",Toast.LENGTH_SHORT).show()
                        Picasso.get().load(it.toString()).into(binding.image)
                    }
                }

            }
        }
    }

    private fun getFiletype(data:Uri?):String?{
        val mimeType = MimeTypeMap.getSingleton()
        return  mimeType.getMimeTypeFromExtension(data?.let { contentResolver.getType(data!! )})

    }
}