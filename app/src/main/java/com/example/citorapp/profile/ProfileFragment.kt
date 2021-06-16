package com.example.citorapp.profile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.citorapp.databinding.FragmentProfileBinding
import com.example.citorapp.utils.Constants
import com.example.citorapp.utils.MySharedPreferences
import dev.shreyaspatil.MaterialDialog.MaterialDialog
import com.example.citorapp.R
import com.example.citorapp.auth.AuthActivity
import com.example.citorapp.auth.login.LoginFragment

class ProfileFragment : Fragment() {

    private lateinit var profileBinding: FragmentProfileBinding
    private lateinit var myPreferences: MySharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        profileBinding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        return profileBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myPreferences = MySharedPreferences(requireContext())

        val userName = myPreferences.getValue(Constants.USER_NAMA).toString()
        profileBinding.tvUserName.text = userName

        val foto = myPreferences.getValue(Constants.USER_FOTO).toString()
        Glide.with(requireContext())
            .load(foto)
            .apply(RequestOptions().override(250))
            .placeholder(R.drawable.user_photo_icon)
            .error(R.drawable.user_photo_icon)
            .into(profileBinding.ivUserProfile)

        profileBinding.btnLogout.setOnClickListener {
            val mDialog = MaterialDialog.Builder(requireContext() as Activity)
                .setTitle("Logout")
                .setMessage(getString(R.string.confirm_logout))
                .setCancelable(true)
                .setPositiveButton(
                    getString(R.string.no), R.drawable.dialog_close
                ) { dialogInterface, which ->
                    dialogInterface.dismiss()
                }
                .setNegativeButton(
                    getString(R.string.yes), R.drawable.logout_icon
                ) { dialogInterface, which ->
                    myPreferences.setValue(Constants.USER, "")
                    myPreferences.setValue(Constants.USER_ID, "")
                    myPreferences.setValue(Constants.USER_NAMA, "")
                    myPreferences.setValue(Constants.USER_EMAIL, "")
                    myPreferences.setValue(Constants.USER_NOHP, "")
                    myPreferences.setValue(Constants.DEVICE_TOKEN, "")

                    startActivity(Intent(context, AuthActivity::class.java))
                    activity?.finish()
                    dialogInterface.dismiss()
                }
                .build()
            // Show Dialog
            mDialog.show()
        }
    }

}