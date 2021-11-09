package com.citor.app.auth.register

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.citor.app.R
import com.citor.app.auth.AuthActivity
import com.citor.app.databinding.ActivityOtpInputBinding
import com.citor.app.retrofit.AuthService
import com.citor.app.retrofit.RetrofitClient
import com.citor.app.retrofit.response.DefaultResponse
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
            ).show()
        }

        otpInputBinding.btnVerifyOtp.setOnClickListener {
            val email = intent.getStringExtra(EmailConfirmActivity.email).toString()
            val otp1 = otpInputBinding.etOtp1.text.toString()
            val otp2 = otpInputBinding.etOtp2.text.toString()
            val otp3 = otpInputBinding.etOtp3.text.toString()
            val otp4 = otpInputBinding.etOtp4.text.toString()
            val otp5 = otpInputBinding.etOtp5.text.toString()
            val otp6 = otpInputBinding.etOtp6.text.toString()
            val otp = otp1 + otp2 + otp3 + otp4 + otp5 + otp6
            otpInputBinding.btnVerifyOtp.startAnimation()
            verifyOtp(email, otp)
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

    private fun verifyOtp(email: String, otp: String) {
        val service = RetrofitClient().apiRequest().create(AuthService::class.java)
        service.verifyOtp(email, otp).enqueue(object : Callback<DefaultResponse> {
            override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                if (response.isSuccessful) {
                    when (response.body()!!.status) {
                        "success" -> {
                            otpInputBinding.btnVerifyOtp.revertAnimation {
                                otpInputBinding.btnVerifyOtp.text = "Berhasil"
                            }
                            val nama = intent.getStringExtra(nama).toString()
                            val nohp = intent.getStringExtra(nohp).toString()
                            val password = intent.getStringExtra(password).toString()
                            registrasi(nama, email, nohp, password)
                        }
                        "not_match" -> {
                            otpInputBinding.btnVerifyOtp.revertAnimation {
                                otpInputBinding.btnVerifyOtp.text = getString(R.string.verify)
                            }
                            Toasty.error(this@OtpInputActivity, R.string.otp_not_match, Toasty.LENGTH_LONG).show()
                        }
                    }
                }
            }

            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                Toasty.error(this@OtpInputActivity, R.string.try_again, Toasty.LENGTH_LONG).show()
            }

        })
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