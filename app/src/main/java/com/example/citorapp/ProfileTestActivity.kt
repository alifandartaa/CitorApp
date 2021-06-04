package com.example.citorapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.citorapp.databinding.ActivityProfileTestBinding
import com.example.citorapp.profile.ProfileFragment

class ProfileTestActivity : AppCompatActivity() {

    private lateinit var profileTestBinding: ActivityProfileTestBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        profileTestBinding = ActivityProfileTestBinding.inflate(layoutInflater)
        setContentView(profileTestBinding.root)
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(profileTestBinding.fragmentContainer.id, ProfileFragment())
        transaction.commit()
    }
}