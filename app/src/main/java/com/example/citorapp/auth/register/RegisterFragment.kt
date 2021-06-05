package com.example.citorapp.auth.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.citorapp.databinding.FragmentRegisterBinding
import com.google.android.material.snackbar.Snackbar

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
        if (activity != null) {
            registerBinding.btnRegister.setOnClickListener {
                if (isDataFilled()) {
                    Snackbar.make(registerBinding.root, "Akun telah dibuat", Snackbar.LENGTH_SHORT)
                        .show()
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
        } else if (!registerBinding.cbTermsRegister.isChecked) {
            registerBinding.cbTermsRegister.error = "Harap centang persyaratan"
            registerBinding.cbTermsRegister.requestFocus()
            return false
        }

        return true
    }
}