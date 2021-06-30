package com.citor.app.auth.register

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.citor.app.R.string
import com.citor.app.databinding.FragmentRegisterBinding
import com.citor.app.retrofit.AuthService
import com.citor.app.retrofit.RetrofitClient
import com.citor.app.retrofit.response.DefaultResponse
import es.dmoral.toasty.Toasty
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterFragment : Fragment() {

    private lateinit var registerBinding: FragmentRegisterBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        registerBinding = FragmentRegisterBinding.inflate(layoutInflater, container, false)
        return registerBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        registerBinding.cbTermsRegister.setOnCheckedChangeListener { buttonView, isChecked ->
            registerBinding.btnRegister.isEnabled = isChecked
        }
        if (activity != null) {
            registerBinding.btnRegister.setOnClickListener {
                if (isDataFilled()) {
//                    Snackbar.make(registerBinding.root, "Akun telah dibuat", Snackbar.LENGTH_SHORT).show()
                    val email = registerBinding.tvValueEmailRegister.text.toString()
                    precheck(email)
                }
            }
        }
    }

    private fun isDataFilled(): Boolean {
        fun String.isValidEmail() =
            isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
        if (registerBinding.tvValueNameRegister.text.toString() == "") {
            registerBinding.tvValueNameRegister.error = "Harap isi nama dengan benar"
            registerBinding.tvValueNameRegister.requestFocus()
            return false
        } else if (registerBinding.tvValueEmailRegister.text.toString() == "") {
            registerBinding.tvValueEmailRegister.error = "Harap isi email dengan benar"
            registerBinding.tvValueEmailRegister.requestFocus()
            return false
        } else if (!registerBinding.tvValueEmailRegister.text.toString().isValidEmail()) {
            registerBinding.tvValueEmailRegister.error = "Format email salah"
            registerBinding.tvValueEmailRegister.requestFocus()
            return false
        } else if (registerBinding.tvValuePhoneRegister.text.toString() == "") {
            registerBinding.tvValuePhoneRegister.error = "Harap isi nomor telepon dengan benar"
            registerBinding.tvValuePhoneRegister.requestFocus()
            return false
        } else if (registerBinding.tvValuePhoneRegister.length() != 12) {
            registerBinding.tvValuePhoneRegister.error = "Harap isi nomor telepon dengan benar"
            registerBinding.tvValuePhoneRegister.requestFocus()
            return false
        } else if (registerBinding.tvValuePasswordRegister.text.toString() == "") {
            registerBinding.tvValuePasswordRegister.error = "Harap isi kata sandi dengan benar"
            registerBinding.tvValuePasswordRegister.requestFocus()
            return false
        }
        return true
    }

    private fun precheck(email: String) {
        val service = RetrofitClient().apiRequest().create(AuthService::class.java)
        service.precheck(email).enqueue(object : Callback<DefaultResponse> {
            override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                if (response.isSuccessful) {
                    when (response.body()!!.status) {
                        "success" -> {
                            val nama = registerBinding.tvValueNameRegister.text.toString()
                            val nohp = registerBinding.tvValuePhoneRegister.text.toString()
                            val password = registerBinding.tvValuePasswordRegister.text.toString()
                            val emailConfirm = Intent(requireContext(), EmailConfirmActivity::class.java)
                                .apply {
                                    putExtra(EmailConfirmActivity.nama, nama)
                                    putExtra(EmailConfirmActivity.email, email)
                                    putExtra(EmailConfirmActivity.nohp, nohp)
                                    putExtra(EmailConfirmActivity.password, password)
                                }
                            startActivity(emailConfirm)
                            activity?.finish()
                        }
                        "failed" -> {
                            Toasty.warning(requireContext(), response.body()!!.message, Toasty.LENGTH_LONG).show()
                        }
                    }
                } else {
                    Toasty.error(requireContext(), string.try_again, Toasty.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                Toasty.error(requireContext(), string.try_again, Toasty.LENGTH_LONG).show()
            }
        })
    }
}