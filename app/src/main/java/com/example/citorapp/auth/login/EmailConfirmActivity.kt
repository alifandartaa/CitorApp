package com.example.citorapp.auth.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.citorapp.databinding.ActivityEmailConfirmBinding

class EmailConfirmActivity : AppCompatActivity() {

    private lateinit var emailConfirmBinding: ActivityEmailConfirmBinding

    companion object {
        const val nama = "nama"
        const val email = "email"
        const val nohp = "nohp"
        const val password = "password"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        emailConfirmBinding = ActivityEmailConfirmBinding.inflate(layoutInflater)
        setContentView(emailConfirmBinding.root)

        val nama = intent.getStringExtra(nama)
        val email = intent.getStringExtra(email)
        val nohp = intent.getStringExtra(nohp)
        val password = intent.getStringArrayExtra(password)

        emailConfirmBinding.tvAskEmailVerify.text = email

        emailConfirmBinding.btnSendOtp.setOnClickListener {
            val otp = Intent(this@EmailConfirmActivity, OtpInputActivity::class.java)
                .apply {
                    putExtra(OtpInputActivity.nama, nama)
                    putExtra(OtpInputActivity.email, email)
                    putExtra(OtpInputActivity.nohp, nohp)
                    putExtra(OtpInputActivity.password, password)
                }
            startActivity(otp)
            finish()
        }
    }
}