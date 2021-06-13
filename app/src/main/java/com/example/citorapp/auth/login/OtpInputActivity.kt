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
import com.example.citorapp.auth.AuthActivity
import com.example.citorapp.databinding.ActivityOtpInputBinding
import com.example.citorapp.retrofit.AuthService
import com.example.citorapp.retrofit.RetrofitClient
import com.example.citorapp.retrofit.response.DefaultResponse
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import es.dmoral.toasty.Toasty
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OtpInputActivity : AppCompatActivity() {

    private lateinit var otpInputBinding: ActivityOtpInputBinding

    companion object {
        const val nama = "nama"
        const val email = "email"
        const val nohp = "nohp"
        const val password = "password"
    }

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
            val nama = intent.getStringExtra(EmailConfirmActivity.nama).toString()
            val email = intent.getStringExtra(EmailConfirmActivity.email).toString()
            val nohp = intent.getStringExtra(EmailConfirmActivity.nohp).toString()
            val password = intent.getStringArrayExtra(EmailConfirmActivity.password).toString()
            registrasi(nama, email, nohp, password)
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
            startActivity(Intent(this@OtpInputActivity, AuthActivity::class.java))
            finish()
        }
        dialog.show()
    }

    private fun registrasi(nama: String, email: String, nohp: String, password: String) {
        val service = RetrofitClient().apiRequest().create(AuthService::class.java)
        service.register(nama, email, nohp, password).enqueue(object : Callback<DefaultResponse> {
            override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                if (response.isSuccessful) {
                    when (response.body()!!.status) {
                        "success" -> {
                            customSuccessDialog()
                        }
                        "failed" -> {
                            Toasty.warning(this@OtpInputActivity, response.body()!!.message, Toasty.LENGTH_LONG).show()
                        }
                    }
                } else {
                    Toasty.error(this@OtpInputActivity, R.string.try_again, Toasty.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                Toasty.error(this@OtpInputActivity, R.string.try_again, Toasty.LENGTH_LONG).show()
            }
        })
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