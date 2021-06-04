package com.example.citorapp.login

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import com.example.citorapp.WasherProfileActivity
import com.example.citorapp.databinding.ActivityOtpInputBinding
import com.google.android.material.snackbar.Snackbar

class OtpInputActivity : AppCompatActivity() {

    private lateinit var otpInputBinding: ActivityOtpInputBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        otpInputBinding = ActivityOtpInputBinding.inflate(layoutInflater)
        setContentView(otpInputBinding.root)

        autoNextOtpCode()
        otpInputBinding.btnResendOtp.setOnClickListener {
            Snackbar.make(otpInputBinding.root, "Kode OTP telah dikirim", Snackbar.LENGTH_SHORT)
                .show()
        }

        otpInputBinding.btnVerifyOtp.setOnClickListener {
            val intent = Intent(this, WasherProfileActivity::class.java)
            startActivity(intent)
        }
    }

    private fun autoNextOtpCode() {
        otpInputBinding.etOtp1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                val codeText = otpInputBinding.etOtp1.text.toString()
                if (codeText.length == 1) {
                    otpInputBinding.etOtp2.requestFocus()
                }
            }
        })

        otpInputBinding.etOtp2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                val codeText = otpInputBinding.etOtp2.text.toString()
                if (codeText.length == 1) {
                    otpInputBinding.etOtp3.requestFocus()
                }
            }
        })

        otpInputBinding.etOtp3.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                val codeText = otpInputBinding.etOtp3.text.toString()
                if (codeText.length == 1) {
                    otpInputBinding.etOtp4.requestFocus()
                }
            }
        })

        otpInputBinding.etOtp4.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                val codeText = otpInputBinding.etOtp4.text.toString()
                if (codeText.length == 1) {
                    otpInputBinding.etOtp5.requestFocus()
                }
            }
        })

        otpInputBinding.etOtp5.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                val codeText = otpInputBinding.etOtp5.text.toString()
                if (codeText.length == 1) {
                    otpInputBinding.etOtp6.requestFocus()
                }
            }
        })
    }
}