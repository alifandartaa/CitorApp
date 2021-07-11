package com.citor.app.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.citor.app.MainActivity
import com.citor.app.R
import com.citor.app.databinding.ActivityEditPassBinding
import com.citor.app.retrofit.AuthService
import com.citor.app.retrofit.RetrofitClient
import com.citor.app.retrofit.response.DefaultResponse
import com.citor.app.utils.Constants
import com.citor.app.utils.MySharedPreferences
import es.dmoral.toasty.Toasty
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditPassActivity : AppCompatActivity() {

    private lateinit var editPassBinding: ActivityEditPassBinding
    private lateinit var myPreferences: MySharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        editPassBinding = ActivityEditPassBinding.inflate(layoutInflater)
        setContentView(editPassBinding.root)

        myPreferences = MySharedPreferences(this@EditPassActivity)
        val idUser = myPreferences.getValue(Constants.USER_ID).toString()

        editPassBinding.btnSubmit.setOnClickListener {
            val pass1 = editPassBinding.tvValuePassword.text.toString()
            val pass2 = editPassBinding.tvValuePasswordValidation.text.toString()
            if (validate()) {
                if (pass1 == pass2) {
                    editPass(idUser, pass1)
                } else {
                    Toasty.error(this@EditPassActivity, "Kata Sandi Tidak Cocok, Silahkan Periksa Kembali", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun validate(): Boolean {
        if (editPassBinding.tvValuePassword.text!!.isEmpty()) {
            editPassBinding.tvValuePassword.requestFocus()
            editPassBinding.tvValuePassword.error = "Masukan password"
            return false
        }
        if (editPassBinding.tvValuePasswordValidation.text!!.isEmpty()) {
            editPassBinding.tvValuePasswordValidation.requestFocus()
            editPassBinding.tvValuePasswordValidation.error = "Masukan password lagi"
            return false
        }
        return true
    }

    private fun editPass(iduser: String, password: String) {
        val service = RetrofitClient().apiRequest().create(AuthService::class.java)
        service.editPass(iduser, password).enqueue(object : Callback<DefaultResponse> {
            override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                if (response.isSuccessful) {
                    if (response.body()!!.status == "success") {
                        Toasty.success(this@EditPassActivity, "Kata Sandi Berhasil Diubah", Toasty.LENGTH_LONG).show()
                        startActivity(Intent(this@EditPassActivity, MainActivity::class.java))
                    }
                }
            }

            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                Toasty.error(this@EditPassActivity, R.string.try_again, Toasty.LENGTH_LONG).show()
            }

        })
    }
}