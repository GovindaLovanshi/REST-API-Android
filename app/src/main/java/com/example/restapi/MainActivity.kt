package com.example.restapi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.restapi.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding :ActivityMainBinding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnCreateNotes.setOnClickListener {
            startActivity(Intent(this,AddNoteActivity::class.java))
        }

        binding.btnNewNotes.setOnClickListener {
            startActivity(Intent(this,AllNotesActivity::class.java))

        }
    }
}