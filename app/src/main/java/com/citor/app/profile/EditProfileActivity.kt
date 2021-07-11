package com.citor.app.profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.citor.app.MainActivity
import com.citor.app.R
import com.citor.app.databinding.ActivityEditProfileBinding
import com.citor.app.retrofit.AuthService
import com.citor.app.retrofit.DataService
import com.citor.app.retrofit.RetrofitClient
import com.citor.app.retrofit.response.DefaultResponse
import com.citor.app.retrofit.response.LoginResponse
import com.citor.app.utils.Constants
import com.citor.app.utils.FileUtils
import com.citor.app.utils.MySharedPreferences
import com.github.dhaval2404.imagepicker.ImagePicker
import es.dmoral.toasty.Toasty
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.*

class EditProfileActivity : AppCompatActivity() {

    private lateinit var editProfileBinding: ActivityEditProfileBinding
    private lateinit var myPreferences: MySharedPreferences
    var photoUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        editProfileBinding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(editProfileBinding.root)

        myPreferences = MySharedPreferences(this@EditProfileActivity)

        val nameTv = myPreferences.getValue(Constants.USER_NAMA)
        val emailTv = myPreferences.getValue(Constants.USER_EMAIL)
        val phoneTv = myPreferences.getValue(Constants.USER_NOHP)
        val fotoTv = myPreferences.getValue(Constants.USER_FOTO).toString()

        editProfileBinding.tvValueNameProfile.setText(nameTv)
        editProfileBinding.tvValueEmailProfile.setText(emailTv)
        editProfileBinding.tvValuePhoneProfile.setText(phoneTv)

        Glide.with(this@EditProfileActivity)
            .load(fotoTv)
            .apply(RequestOptions().override(200))
            .placeholder(R.drawable.user_photo_icon)
            .error(R.drawable.user_photo_icon)
            .into(editProfileBinding.ivUserProfile)

        editProfileBinding.btnChoosePhoto.setOnClickListener {
            ImagePicker.with(this)
                .cropSquare()
                .compress(1024)
                .maxResultSize(720, 720)
                .galleryMimeTypes(  //Exclude gif images
                    mimeTypes = arrayOf(
                        "image/png",
                        "image/jpg",
                        "image/jpeg"
                    )
                )
                .start { resultCode, data ->
                    when (resultCode) {
                        Activity.RESULT_OK -> {
                            val fileUri = data?.data
                            this.photoUri = fileUri
                            editProfileBinding.imgUser.setImageURI(fileUri)
                            val file: File? = ImagePicker.getFile(data)
                            val filePath: String = ImagePicker.getFilePath(data).toString()
                        }
                        ImagePicker.RESULT_ERROR -> {
                            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
                        }
                        else -> {
                            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
        }

        editProfileBinding.btnSubmit.setOnClickListener {
            if (validate()) {
                val idUser = myPreferences.getValue(Constants.USER_ID).toString()
                val idRequestBody = idUser.toRequestBody(MultipartBody.FORM)

                val tokenAuth = myPreferences.getValue(Constants.TokenAuth).toString()

                val name = editProfileBinding.tvValueNameProfile.text.toString()
                val nameRequestBody = name.toRequestBody(MultipartBody.FORM)

                val email = editProfileBinding.tvValueEmailProfile.text.toString()
                val emailRequestBody = email.toRequestBody(MultipartBody.FORM)

                val phone = editProfileBinding.tvValuePhoneProfile.text.toString()
                val phoneRequestBody = phone.toRequestBody(MultipartBody.FORM)

//                val password = editProfileBinding.tvValuePasswordProfile.text.toString()
//                val passwordRequestBody = password.toRequestBody(MultipartBody.FORM)

                var photo: MultipartBody.Part? = null
                photoUri?.let {
                    val file = FileUtils.getFile(this, photoUri)
                    val requestBodyPhoto = RequestBody.create(
                        contentResolver.getType(it).toString().toMediaTypeOrNull(), file
                    )
                    photo = MultipartBody.Part.createFormData("filefoto", file.name, requestBodyPhoto)
                }
                val service = RetrofitClient().apiRequest().create(DataService::class.java)
                service.editprofile(
                    idRequestBody,
                    nameRequestBody,
                    emailRequestBody,
                    phoneRequestBody,
                    photo,
                    "Bearer $tokenAuth"
                )
                    .enqueue(object : Callback<DefaultResponse> {
                        override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                            if (response.isSuccessful) {
                                if (response.body()!!.status == "success") {
                                    myPreferences.setValue(Constants.USER_NAMA, editProfileBinding.tvValueNameProfile.text.toString())
                                    myPreferences.setValue(Constants.USER_EMAIL, editProfileBinding.tvValueEmailProfile.text.toString())
                                    myPreferences.setValue(Constants.USER_NOHP, editProfileBinding.tvValuePhoneProfile.text.toString())
                                    getUserFoto(idUser)
                                    Toasty.success(this@EditProfileActivity, "Sukses", Toasty.LENGTH_LONG).show()
                                    startActivity(Intent(this@EditProfileActivity, MainActivity::class.java))
                                } else {
                                    Toasty.error(this@EditProfileActivity, response.message(), Toasty.LENGTH_LONG).show()
                                }
                            }
                        }

                        override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                            TODO("Not yet implemented")
                        }

                    })
            }
        }
    }

    private fun getUserFoto(iduser: String) {
        val service = RetrofitClient().apiRequest().create(AuthService::class.java)
        service.getUserFoto(iduser).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    if (response.body()!!.status == "success") {
                        myPreferences.setValue(Constants.USER_FOTO, response.body()!!.data[0].foto_path)
                    }
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toasty.error(this@EditProfileActivity, R.string.try_again, Toasty.LENGTH_LONG).show()
            }

        })
    }

    private fun validate(): Boolean {

        fun String.isValidEmail() =
            isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()

        if (editProfileBinding.tvValueNameProfile.text.toString() == "") {
            editProfileBinding.tvValueNameProfile.error = "Harap isi nama dengan benar"
            editProfileBinding.tvValueNameProfile.requestFocus()
            return false
        } else if (editProfileBinding.tvValueEmailProfile.text.toString() == "") {
            editProfileBinding.tvValueEmailProfile.error = "Harap isi email dengan benar"
            editProfileBinding.tvValueEmailProfile.requestFocus()
            return false
        } else if (!editProfileBinding.tvValueEmailProfile.text.toString().isValidEmail()) {
            editProfileBinding.tvValueEmailProfile.error = "Format email salah"
            editProfileBinding.tvValueEmailProfile.requestFocus()
            return false
        } else if (editProfileBinding.tvValuePhoneProfile.text.toString() == "") {
            editProfileBinding.tvValuePhoneProfile.error = "Harap isi nomor telepon dengan benar"
            editProfileBinding.tvValuePhoneProfile.requestFocus()
            return false
        } else if (editProfileBinding.tvValuePhoneProfile.length() != 12) {
            editProfileBinding.tvValuePhoneProfile.error = "Harap isi nomor telepon dengan benar"
            editProfileBinding.tvValuePhoneProfile.requestFocus()
            return false
        }
        return true

    }
}