package com.example.citorapp.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.citorapp.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private lateinit var loginBinding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        loginBinding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        return loginBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (activity != null) {
            loginBinding.btnLogin.setOnClickListener {
                if (isDataFilled()) {
                    val intent = Intent(activity, OtpInputActivity::class.java)
                    startActivity(intent)
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
}