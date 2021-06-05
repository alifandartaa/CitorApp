package com.example.citorapp.auth.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.citorapp.databinding.ActivityEmailConfirmBinding

class EmailConfirmActivity : AppCompatActivity() {

    private lateinit var emailConfirmBinding: ActivityEmailConfirmBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        emailConfirmBinding = ActivityEmailConfirmBinding.inflate(layoutInflater)
        setContentView(emailConfirmBinding.root)

        emailConfirmBinding.btnSendOtp.setOnClickListener {
            val intent = Intent(this, OtpInputActivity::class.java)
            startActivity(intent)
        }
    }
}