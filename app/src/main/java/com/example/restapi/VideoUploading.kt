package com.example.restapi

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.MimeTypeMap
import android.widget.MediaController
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import com.example.restapi.databinding.ActivityVideoUploadingBinding
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class VideoUploading : AppCompatActivity() {
    private lateinit var binding: ActivityVideoUploadingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoUploadingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.videoView.isVisible = false
        binding.videobtn.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_PICK
            intent.type = "video/*"
            videolauncher.launch(intent)

        }
    }

    val videolauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val ref = Firebase.storage.reference.child(
                    "Video/" + System.currentTimeMillis() + "." + getFiletype(it.data!!.data)
                )

                // upload a single file
//            if(it.data!=null){
//                binding.videobtn.isVisible = false
//                binding.videoView.isVisible = true
//                val mediaController = MediaController(this)
//                mediaController.setAnchorView(binding.videoView)
//                binding.videoView.setVideoURI(it.data!!.data)
//                binding.videoView.setMediaController(mediaController)
//                binding.videoView.start()
//            }
                
                // upload multiple file

                if (it.data != null) {
                    ref.putFile(it.data!!.data!!).addOnSuccessListener {
                        ref.downloadUrl.addOnSuccessListener {
                            Firebase.database.reference.child("Video").push()
                                .setValue(it.toString())

                            Toast.makeText(
                                this@VideoUploading,
                                "Video Uploaded",
                                Toast.LENGTH_SHORT
                            ).show()
                            binding.videobtn.isVisible = false
                            binding.videoView.isVisible = true
                            val mediaController = MediaController(this)
                            mediaController.setAnchorView(binding.videoView)
                            binding.videoView.setVideoURI(it)
                            binding.videoView.setMediaController(mediaController)
                            binding.videoView.start()
                        }
                    }
                }

            }
        }

    private fun getFiletype(data: Uri?): String? {
        val mimeType = MimeTypeMap.getSingleton()
        return mimeType.getMimeTypeFromExtension(data?.let { contentResolver.getType(data!!) })

    }
}