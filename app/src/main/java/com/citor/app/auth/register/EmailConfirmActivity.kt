package com.citor.app.auth.register

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.citor.app.R
import com.citor.app.databinding.ActivityEmailConfirmBinding
import com.citor.app.retrofit.AuthService
import com.citor.app.retrofit.RetrofitClient
import com.citor.app.retrofit.response.DefaultResponse
import es.dmoral.toasty.Toasty
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

        val email = intent.getStringExtra(email)
        emailConfirmBinding.tvAskEmailVerify.text = email

        emailConfirmBinding.btnBack.setOnClickListener {
            onBackPressed()
        }

        emailConfirmBinding.btnSendOtp.setOnClickListener {
            emailConfirmBinding.btnSendOtp.startAnimation()
            requestOtp("$email")
        }
    }

    private fun requestOtp(email: String) {
        val service = RetrofitClient().apiRequest().create(AuthService::class.java)
        service.requestOtp(email).enqueue(object : Callback<DefaultResponse> {
            override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                if (response.isSuccessful) {
                    if (response.body()!!.status == "success") {
                        emailConfirmBinding.btnSendOtp.revertAnimation {
                            emailConfirmBinding.btnSendOtp.text = "Terkirim"
                        }

                        val nama = intent.getStringExtra(nama)
                        val nohp = intent.getStringExtra(nohp)
                        val password = intent.getStringExtra(password)
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

            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                Toasty.error(this@EmailConfirmActivity, R.string.try_again, Toasty.LENGTH_LONG).show()
            }

        })
    }
}