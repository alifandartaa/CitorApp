package com.citor.app.profile


import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.citor.app.MainActivity
import com.citor.app.databinding.ActivityEditProfileBinding
import com.citor.app.retrofit.DataService
import com.citor.app.retrofit.RetrofitClient
import com.citor.app.retrofit.response.DefaultResponse
import com.citor.app.utils.Constants
import com.citor.app.utils.FileUtils
import com.citor.app.utils.MySharedPreferences
import com.github.dhaval2404.imagepicker.ImagePicker
import com.github.dhaval2404.imagepicker.ImagePicker.Companion.getFile
import es.dmoral.toasty.Toasty
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create
import java.io.File
import java.util.*


class EditProfileActivity : AppCompatActivity(){

    private lateinit var editProfileBinding: ActivityEditProfileBinding
    private lateinit var myPreferences: MySharedPreferences
    var photoUri : Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        editProfileBinding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(editProfileBinding.root)

        myPreferences = MySharedPreferences(this@EditProfileActivity)


        editProfileBinding.btnChoosePhoto.setOnClickListener{
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
                    if(resultCode == Activity.RESULT_OK){
                        val fileUri = data?.data
                        this.photoUri = fileUri
                        editProfileBinding.imgUser.setImageURI(fileUri)

                        val file : File? = ImagePicker.getFile(data)
                        val filePath: String = ImagePicker.getFilePath(data).toString()
                    }
                    else if(resultCode == ImagePicker.RESULT_ERROR){
                        Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
                    }
                    else{
                        Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        editProfileBinding.btnSubmit.setOnClickListener{
            if(validate()){
                val idUser = myPreferences.getValue(Constants.USER_ID).toString()
                val idRequestBody = idUser.toRequestBody(MultipartBody.FORM)

                val tokenAuth = myPreferences.getValue(Constants.TokenAuth).toString()

                val name = editProfileBinding.tvValueNameProfile.text.toString()
                val nameRequestBody = name.toRequestBody(MultipartBody.FORM)

                val email = editProfileBinding.tvValueEmailProfile.text.toString()
                val emailRequestBody = email.toRequestBody(MultipartBody.FORM)

                val phone = editProfileBinding.tvValuePhoneProfile.text.toString()
                val phoneRequestBody = phone.toRequestBody(MultipartBody.FORM)

                val password = editProfileBinding.tvValuePasswordProfile.text.toString()
                val passwordRequestBody = password.toRequestBody(MultipartBody.FORM)

                var photo: MultipartBody.Part? = null
                photoUri?.let{
                    val file = FileUtils.getFile(this, photoUri)
                    val requestBodyPhoto = RequestBody.create(
                        contentResolver.getType(it).toString().toMediaTypeOrNull(), file)
                    photo = MultipartBody.Part.createFormData("filefoto", file.name, requestBodyPhoto)
                }
                val service = RetrofitClient().apiRequest().create(DataService::class.java)
                service.editprofile(idRequestBody, nameRequestBody, emailRequestBody, phoneRequestBody, passwordRequestBody, photo, "Bearer $tokenAuth")
                    .enqueue(object : Callback<DefaultResponse>{
                        override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                            if(response.isSuccessful){
                                if(response.body()!!.status == "success"){
                                    Toasty.success(this@EditProfileActivity, "Sukses", Toasty.LENGTH_LONG).show()
                                    startActivity(Intent(this@EditProfileActivity, MainActivity::class.java))
                                }
                                else{
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
        } else if (editProfileBinding.tvValuePasswordProfile.text.toString() == "") {
            editProfileBinding.tvValuePasswordProfile.error = "Harap isi kata sandi dengan benar"
            editProfileBinding.tvValuePasswordProfile.requestFocus()
            return false
        }
        return true

    }

//    override fun onClick(v: View?) {
//        when(v?.id){
//            R.id.btn_choose_photo -> MaterialDialog.Builder(this)
//                .title("Upload Images")
//                .items(R.array.uploadImages)
//                .items(R.array.itemIds)
//                .itemsCallback{dialog, view, which, text ->
//                    when(which){
//                        0 -> {
//                            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//                            startActivityForResult(galleryIntent, REQUEST_PICK_PHOTO)
//
////                            val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
////                                if (result.resultCode == Activity.RESULT_OK) {
////                                    val intent = result.data
////                                    // Handle the Intent
////                                }
////                            }
////
////                            startForResult.launch(Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI))
//
//
//                        }
//                        1 -> captureImage()
//                        2 -> imageView.setImageResource(R.drawable.ic_launcher)
//                    }
//                }
//                .show()
//            R.id.btn_submit -> uploadFile()
//        }
//    }
//
//
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if(resultCode == Activity.RESULT_OK){
//            if(requestCode == REQUEST_TAKE_PHOTO || requestCode == REQUEST_PICK_PHOTO){
//                if(data != null){
//                    val selectedImage = data.data
//
//                    val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
//
//                    val cursor = contentResolver.query(selectedImage!!, filePathColumn, null, null, null)
//                    assert(cursor != null)
//                    cursor!!.moveToFirst()
//
//                    val columnIndex = cursor.getColumnIndex(filePathColumn[0])
//                    mediaPath = cursor.getString(columnIndex)
//
//                    imageView.setImageBitmap(BitmapFactory.decodeFile(mediaPath))
//                    cursor.close()
//
//                    postPath = mediaPath
//                }
//            }
//
//            else if(requestCode == CAMERA_PIC_REQUEST){
//                if(Build.VERSION.SDK_INT > 21){
////                    Glide.with(this).load(mImageFileLocation).into(imageView)
////                    postPath = mImageFileLocation
//                    onCaptureImageResult(data)
//                }
//                else{
////                    Glide.with(this).load(mImageFileLocation).into(imageView)
////                    postPath = fileUri!!.path
//                }
//            }
//        }
//        else if (resultCode != Activity.RESULT_CANCELED) {
//            Toast.makeText(this, "Sorry, there was an error!", Toast.LENGTH_LONG).show()
//        }
//    }
//
//    private fun onCaptureImageResult(data: Intent?) {
//        val bmp = data?.extras?.get("data") as Bitmap
//        val bytes = ByteArrayOutputStream()
//        bmp.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
//        val bb = bytes.toByteArray()
//        fileImageUpload = bb
//        imageView?.setImageBitmap(bmp)
//    }
//
//    protected fun initDialog() {
//
//        pDialog = ProgressDialog(this)
//        pDialog.setMessage(getString(R.string.msg_loading))
//        pDialog.setCancelable(true)
//    }
//
//
//    protected fun showpDialog() {
//
//        if (!pDialog.isShowing) pDialog.show()
//    }
//
//    protected fun hidepDialog() {
//
//        if (pDialog.isShowing) pDialog.dismiss()
//    }
//
//    private fun captureImage(){
//        if(Build.VERSION.SDK_INT > 21){ ////use this if Lollipop_Mr1 (API 22) or above
//            val callCameraApplicationIntent = Intent()
//
//            callCameraApplicationIntent.action = MediaStore.ACTION_IMAGE_CAPTURE
//
//            //We give some instruction to the intent to save the image
//            var photoFile: File? = null
//
//            val permissionCheck = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
//
//
//            try{
//                // If the createImageFile will be successful, the photo file will have the address of the file
//                photoFile = createImageFile()
//
//                // Here we call the function that will try to catch the exception made by the throw function
//            }
//            catch (e: IOException){
//                Logger.getAnonymousLogger().info("Exception error in generating the file")
//                e.printStackTrace()
//            }
//
//            if (permissionCheck == PackageManager.PERMISSION_GRANTED){
//                // Here we add an extra file to the intent to put the address on to. For this purpose we use the FileProvider, declared in the AndroidManifest.
//                val outputUri = photoFile?.let { FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".fileprovider", it) }
//
//                callCameraApplicationIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri)
//
//                // The following is a new line with a trying attempt
//                callCameraApplicationIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
//
//                Logger.getAnonymousLogger().info("Calling the camera App by intent")
//
//                // The following strings calls the camera app and wait for his file in return.
//                startActivityForResult(callCameraApplicationIntent, CAMERA_PIC_REQUEST)
//            }
//
//            else{
//                ActivityCompat.requestPermissions(
//                    this,
//                    arrayOf(android.Manifest.permission.CAMERA),
//                    CAMERA_PERMISSION_CODE
//                )
//            }
//
//
//        }
//        else{
//            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//
//            fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE)
//
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
//
//            //start the image capture intent
//            startActivityForResult(intent, CAMERA_PIC_REQUEST)
//        }
//    }
//
//    @Throws(IOException::class)
//    internal fun createImageFile(): File{
//        Logger.getAnonymousLogger().info("Generating the image - method started")
//        // Here we create a "non-collision file name", alternatively said, "an unique filename" using the "timeStamp" functionality
//        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmSS").format(Date())
//        val imageFileName = "IMAGE_" + timeStamp
//        // Here we specify the environment location and the exact path where we want to save the so-created file
//        val storageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/photo_saving_app")
//        Logger.getAnonymousLogger().info("Storage directory set")
//
//        // Then we create the storage directory if does not exists
//        if (!storageDirectory.exists()) storageDirectory.mkdir()
//
//        // Here we create the file using a prefix, a suffix and a directory
//        val image = File(storageDirectory, imageFileName + ".jpg")
//        // File image = File.createTempFile(imageFileName, ".jpg", storageDirectory);
//
//        // Here the location is saved into the string mImageFileLocation
//        Logger.getAnonymousLogger().info("File name and path set")
//
//        mImageFileLocation = image.absolutePath
//        // fileUri = Uri.parse(mImageFileLocation);
//        // The file is returned to the previous intent across the camera application
//        return image
//    }
//
//    /**
//     * Here we store the file url as it will be null after returning from camera
//     * app
//     */
//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//
//        // save file url in bundle as it will be null on screen orientation
//        // changes
//        outState.putParcelable("file_uri", fileUri)
//    }
//
//    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
//        super.onRestoreInstanceState(savedInstanceState)
//
//        // get the file url
//        fileUri = savedInstanceState.getParcelable("file_uri")
//    }
//
//
//    /**
//     * Receiving activity result method will be called after closing the camera
//     */
//
//    /**
//     * ------------ Helper Methods ----------------------
//     */
//
//    /**
//     * Creating file uri to store image/video
//     */
//
//    private fun getOutputMediaFileUri(type: Int): Uri {
//        return Uri.fromFile(getOutputMediaFile(type))
//    }
//
//    private fun uploadFile(){
//        if(postPath == null || postPath == ""){
//            Toast.makeText(this, "Tolong Pilihlah Foto", Toast.LENGTH_LONG).show()
//            return
//        }
//        else{
//            showpDialog()
//
//            // Map is used to multipart the file using okhttp3.RequestBody
//            val map = HashMap<String, RequestBody>()
//            val file = File(postPath!!)
//
//            //request
//
//
//
//
//        }
//    }
//
//    companion object {
//        private val REQUEST_TAKE_PHOTO = 0
//        private val REQUEST_PICK_PHOTO = 2
//        private val CAMERA_PIC_REQUEST = 1111
//        private val CAMERA_PERMISSION_CODE = 100
//
//        private val TAG = EditProfileActivity::class.java.getSimpleName()
//
//        private val CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100
//
//        val MEDIA_TYPE_IMAGE = 1
//        val IMAGE_DIRECTORY_NAME = "Android File Upload"
//
//        /**
//         * returning image / video
//         */
//
//        private fun getOutputMediaFile(type: Int): File?{
//            //External sdcard location
//            val mediaStorageDir = File(
//                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), IMAGE_DIRECTORY_NAME
//            )
//
//            //create the storage directory if it does not exist
//
//            if(!mediaStorageDir.exists()){
//                if(!mediaStorageDir.mkdirs()){
//                    Log.d(TAG, "Oops! Failed create "
//                            + IMAGE_DIRECTORY_NAME + " directory")
//                    return null
//                }
//            }
//            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss",
//                Locale.getDefault()).format(Date())
//            val mediaFile: File
//            if (type == MEDIA_TYPE_IMAGE) {
//                mediaFile = File(mediaStorageDir.path + File.separator
//                        + "IMG_" + ".jpg")
//            } else {
//                return null
//            }
//
//            return mediaFile
//        }
//    }
}