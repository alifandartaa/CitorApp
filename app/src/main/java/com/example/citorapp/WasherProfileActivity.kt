package com.example.citorapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.citorapp.databinding.ActivityWasherProfileBinding

class WasherProfileActivity : AppCompatActivity() {

    private lateinit var washerProfileBinding: ActivityWasherProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        washerProfileBinding = ActivityWasherProfileBinding.inflate(layoutInflater)
        setContentView(washerProfileBinding.root)

        washerProfileBinding.tvVendorMap.setOnClickListener {
            val intent = Intent(this, ProfileTestActivity::class.java)
            startActivity(intent)
        }
    }
}