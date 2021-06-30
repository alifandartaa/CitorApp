package com.citor.mitra.auth.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.citor.mitra.MainActivity
import com.citor.mitra.R
import com.citor.mitra.databinding.FragmentLoginBinding
import com.citor.mitra.retrofit.AuthService
import com.citor.mitra.retrofit.RetrofitClient
import com.citor.mitra.retrofit.response.LoginResponse
import com.citor.mitra.utils.Constants
import com.citor.mitra.utils.MySharedPreferences
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
                val nohp = loginBinding.tvValueNohpLogin.text.toString()
                val pass = loginBinding.tvValuePasswordLogin.text.toString()
                if (isDataFilled()) {
                    loginBinding.btnLogin.startAnimation()
                    loginprocess(nohp, pass)
                }
            }
        }
    }

    private fun isDataFilled(): Boolean {
        if (loginBinding.tvValueNohpLogin.text.toString() == "") {
            loginBinding.tvValueNohpLogin.error = "Harap isi email dengan benar"
            loginBinding.tvValueNohpLogin.requestFocus()
            return false
        } else if (loginBinding.tvValuePasswordLogin.text.toString() == "") {
            loginBinding.tvValuePasswordLogin.error = "Harap isi password dengan benar"
            loginBinding.tvValuePasswordLogin.requestFocus()
            return false
        }
        return true
    }

    private fun loginprocess(nohp: String, password: String) {
        val service = RetrofitClient().apiRequest().create(AuthService::class.java)
        service.login(nohp, password).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    when (response.body()!!.status) {
                        "success" -> {
                            loginBinding.btnLogin.revertAnimation {
                                loginBinding.btnLogin.text = "Berhasil"
                            }
                            myPreferences.setValue(Constants.USER, Constants.LOGIN)
                            myPreferences.setValue(Constants.VENDOR_ID, response.body()!!.data[0].idmitra)
                            myPreferences.setValue(Constants.VENDOR_OWNER, response.body()!!.data[0].nama_owner)
                            myPreferences.setValue(Constants.VENDOR_NAMA, response.body()!!.data[0].nama_mitra)
                            myPreferences.setValue(Constants.VENDOR_ADDRS, response.body()!!.data[0].alamat_mitra)
                            myPreferences.setValue(Constants.VENDOR_NOHP, response.body()!!.data[0].nohp)
                            myPreferences.setValue(Constants.VENDOR_STATUS, response.body()!!.data[0].statusBuka)
                            myPreferences.setValue(Constants.LAT, response.body()!!.data[0].lat)
                            myPreferences.setValue(Constants.LONG, response.body()!!.data[0].long)
                            myPreferences.setValue(Constants.DEVICE_TOKEN, response.body()!!.data[0].device_token)
                            myPreferences.setValue(Constants.VENDOR_FOTO, response.body()!!.data[0].foto_path)
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