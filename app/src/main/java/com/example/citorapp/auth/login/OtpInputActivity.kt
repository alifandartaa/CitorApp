package com.example.citorapp.auth.login

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.example.citorapp.R
import com.example.citorapp.databinding.ActivityOtpInputBinding
import com.example.citorapp.detailVendor.DetailVendorActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar

class OtpInputActivity : AppCompatActivity() {

    private lateinit var otpInputBinding: ActivityOtpInputBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        otpInputBinding = ActivityOtpInputBinding.inflate(layoutInflater)
        setContentView(otpInputBinding.root)

        autoNextOtpCode()
        otpInputBinding.btnResendOtp.setOnClickListener {
            Snackbar.make(
                otpInputBinding.root,
                getString(R.string.otp_already_send),
                Snackbar.LENGTH_SHORT
            )
                .show()
        }

        otpInputBinding.btnVerifyOtp.setOnClickListener {
            customSuccessDialog()
        }
    }

    private fun customSuccessDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.verify_success_dialog)

        val btnComplete = dialog.findViewById<MaterialButton>(R.id.btn_complete)
        btnComplete.setOnClickListener {
            dialog.dismiss()
            val intent = Intent(this, DetailVendorActivity::class.java)
            startActivity(intent)
        }
        dialog.show()
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