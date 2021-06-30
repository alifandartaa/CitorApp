package com.citor.app.auth.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.citor.app.MainActivity
import com.citor.app.R
import com.citor.app.databinding.FragmentLoginBinding
import com.citor.app.retrofit.AuthService
import com.citor.app.retrofit.RetrofitClient
import com.citor.app.retrofit.response.LoginResponse
import com.citor.app.utils.Constants
import com.citor.app.utils.MySharedPreferences
import es.dmoral.toasty.Toasty
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginFragment : Fragment() {

    private lateinit var loginBinding: FragmentLoginBinding
    private lateinit var myPreferences: MySharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        loginBinding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        return loginBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myPreferences = MySharedPreferences(requireContext())

        //Agar program dibawah line ini tidak dijalankan
        if (myPreferences.getValue(Constants.USER).equals(Constants.LOGIN)) {
            startActivity(Intent(requireContext(), MainActivity::class.java))
            activity?.finish()
            return
        }

        if (activity != null) {
            loginBinding.btnLogin.setOnClickListener {
                val email = loginBinding.tvValueEmailLogin.text.toString()
                val pass = loginBinding.tvValuePasswordLogin.text.toString()
                if (isDataFilled()) {
                    loginBinding.btnLogin.startAnimation()
                    loginprocess(email, pass)
                }
            }
        }
    }

    private fun isDataFilled(): Boolean {
        fun String.isValidEmail() =
            isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
        if (loginBinding.tvValueEmailLogin.text.toString() == "") {
            loginBinding.tvValueEmailLogin.error = "Harap isi email dengan benar"
            loginBinding.tvValueEmailLogin.requestFocus()
            return false
        } else if (!loginBinding.tvValueEmailLogin.text.toString().isValidEmail()) {
            loginBinding.tvValueEmailLogin.error = "Format email salah"
            loginBinding.tvValueEmailLogin.requestFocus()
            return false
        } else if (loginBinding.tvValuePasswordLogin.text.toString() == "") {
            loginBinding.tvValuePasswordLogin.error = "Harap isi password dengan benar"
            loginBinding.tvValuePasswordLogin.requestFocus()
            return false
        }
        return true
    }

    private fun loginprocess(email: String, password: String) {
        val service = RetrofitClient().apiRequest().create(AuthService::class.java)
        service.login(email, password).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    when (response.body()!!.status) {
                        "success" -> {
                            loginBinding.btnLogin.revertAnimation {
                                loginBinding.btnLogin.text = "Berhasil"
                            }
                            myPreferences.setValue(Constants.USER, Constants.LOGIN)
                            myPreferences.setValue(Constants.USER_ID, response.body()!!.data[0].iduser)
                            myPreferences.setValue(Constants.USER_NAMA, response.body()!!.data[0].nama)
                            myPreferences.setValue(Constants.USER_EMAIL, response.body()!!.data[0].email)
                            myPreferences.setValue(Constants.USER_NOHP, response.body()!!.data[0].nohp)
                            myPreferences.setValue(Constants.USER_POIN, response.body()!!.data[0].poin)
                            myPreferences.setValue(Constants.DEVICE_TOKEN, response.body()!!.data[0].device_token)
                            myPreferences.setValue(Constants.USER_FOTO, response.body()!!.data[0].foto_path)
                            myPreferences.setValue(Constants.TokenAuth, response.body()!!.tokenAuth)
                            startActivity(Intent(requireContext(), MainActivity::class.java))
                            activity?.finish()
                        }
                        "failed" -> {
                            loginBinding.btnLogin.revertAnimation {
                                loginBinding.btnLogin.text = getString(R.string.login)
                            }
                            Toasty.error(requireContext(), R.string.email_pass_not_match, Toasty.LENGTH_LONG).show()
                        }
                        "not_exist" -> {
                            loginBinding.btnLogin.revertAnimation {
                                loginBinding.btnLogin.text = getString(R.string.login)
                            }
                            Toasty.error(requireContext(), R.string.email_not_registered, Toasty.LENGTH_LONG).show()
                        }
                    }
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toasty.error(requireContext(), R.string.try_again, Toasty.LENGTH_LONG).show()
            }

        })
    }
}