@file:Suppress("RedundantExplicitType", "RedundantExplicitType", "RedundantExplicitType", "RedundantExplicitType")

package com.vedas.apna.Home.View

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.nostra13.universalimageloader.core.ImageLoader
import com.vedas.apna.BuildConfig
import com.vedas.apna.Helper.ProgressDialog
import com.vedas.apna.Helper.UniversalImageLoader
import com.vedas.apna.R
import com.vedas.apna.ServerConnections.RetrofitCallbacks
import com.vedas.apna.ServerConnections.ServerApiCollection
import com.yalantis.ucrop.UCrop
import com.yalantis.ucrop.util.FileUtils
import org.json.JSONException
import org.json.JSONObject
import uk.co.senab.photoview.PhotoView
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*

class UpdateProfileActivity : AppCompatActivity(),RetrofitCallbacks.ServerResponseInterface {
    private var bitmap: Bitmap? = null
    private lateinit var photoView: PhotoView
    private lateinit var rl_cancel: RelativeLayout
    private lateinit var rl_edit: RelativeLayout
    private lateinit var img_edit: ImageView
    lateinit var img_back: ImageView
    private val GALLERY = 1
    private val CAMERA = 2
    var image = ""

    lateinit var pref : SharedPreferences
    lateinit var mobile:String
    lateinit var profilepic:String
    lateinit var name:String
    lateinit var accesstoken:String
    lateinit var email:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)

        pref = getSharedPreferences("LoginPref", 0)
        accesstoken = pref.getString("accesstoken", null).toString()
        name = pref.getString("name", null).toString()
        profilepic = pref.getString("profilepic", null).toString()
        mobile = pref.getString("mobileNumber", null).toString()
        email = pref.getString("userEmail", null).toString()
        init()
    }

    private fun init() {
        photoView = findViewById(R.id.photoView)
        rl_cancel = findViewById(R.id.rl_cancel)
        rl_edit = findViewById(R.id.rl_edit)
        img_back = findViewById(R.id.img_back)
        img_edit = findViewById(R.id.img_edit)
        rl_edit.visibility = View.VISIBLE
        rl_cancel.setOnClickListener(closeclick)
        img_back.setOnClickListener(closeclick)
        rl_edit.setOnClickListener(rl_editclick)
        img_edit.setOnClickListener(rl_editclick)
        RetrofitCallbacks.getInstace().initializeServerInterface(this)
        UniversalImageLoader.setImage(
            profilepic.substring(1),
            photoView,
            null,
            ServerApiCollection.BASE_URL1
        )
    }

    private var closeclick = View.OnClickListener { onBackPressed() }
    private var rl_editclick = View.OnClickListener {requestMultiplePermissions() }

    private fun requestMultiplePermissions() {
        Dexter.withActivity(this)
                .withPermissions(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
                )
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            if (ContextCompat.checkSelfPermission(
                                    this@UpdateProfileActivity,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                                ) != PackageManager.PERMISSION_GRANTED &&
                                ContextCompat.checkSelfPermission(
                                    this@UpdateProfileActivity,
                                    Manifest.permission.CAMERA
                                ) != PackageManager.PERMISSION_GRANTED &&
                                ContextCompat.checkSelfPermission(
                                    this@UpdateProfileActivity,
                                    Manifest.permission.READ_EXTERNAL_STORAGE
                                ) != PackageManager.PERMISSION_GRANTED
                            ) {
                                requestMultiplePermissions()
                            } else {
                                showPictureDialog()
                            }
                            //Toast.makeText(getApplicationContext(), "All permissions are granted by user!", Toast.LENGTH_SHORT).show();
                        } else if (report.isAnyPermissionPermanentlyDenied) {
                            // show alert dialog navigating to Settings
                            //openSettingsDialog();
                            requestMultiplePermissions()
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permissions: List<PermissionRequest?>?,
                        token: PermissionToken
                    ) {
                        token.continuePermissionRequest()
                    }
                }).withErrorListener { Toast.makeText(applicationContext, "Some Error! ", Toast.LENGTH_SHORT).show() }
                .onSameThread()
                .check()
    }
    @SuppressLint("InflateParams")
    private fun showPictureDialog() {
        val dialogView: View = layoutInflater.inflate(R.layout.choose_image, null)
        val dialog = BottomSheetDialog(this@UpdateProfileActivity, R.style.BottomSheetDialogTheme)
        dialog.setContentView(dialogView)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)
        dialog.show()
        val cam: TextView? = dialog.findViewById(R.id.camera)
        val gallery: TextView? = dialog.findViewById(R.id.gallery)
        cam!!.setOnClickListener {
            dialog.dismiss()
            takePhotoFromCamera()
        }
        gallery!!.setOnClickListener {
            dialog.dismiss()
            choosePhotoFromGallary()
        }
    }

    private fun choosePhotoFromGallary() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, GALLERY)
    }

    private fun takePhotoFromCamera() {
        val pictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val file: File = try {
            createImageFile() // 1
        } catch (e: Exception) {
            e.printStackTrace()
            return
        }
        val uri: Uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) // 2
            FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", file) else Uri.fromFile(
            file
        ) // 3
        pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri) // 4
        startActivityForResult(pictureIntent, CAMERA)
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        @SuppressLint("SimpleDateFormat") val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(
            Date()
        )
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val images = File.createTempFile(
            imageFileName,  /* prefix */
            ".jpg",  /* suffix */
            storageDir /* directory */
        )
        // Save a file: path for use with ACTION_VIEW intents
        image = "file:" + images.absolutePath
        return images
    }
    private fun openCropActivity(sourceUri: Uri, destinationUri: Uri) {
        val options = UCrop.Options()
        options.setCompressionQuality(100)
        options.setToolbarColor(ContextCompat.getColor(this, R.color.blue))
        options.setStatusBarColor(ContextCompat.getColor(this, R.color.blue))
        options.setActiveWidgetColor(ContextCompat.getColor(this, R.color.blue))
        UCrop.of(sourceUri, destinationUri)
                .withMaxResultSize(1000, 1000)
                .withAspectRatio(5f, 5f)
                .withOptions(options)
                .start(this@UpdateProfileActivity)
    }
    private fun showImage(imageUri: Uri) {
        try {
            val file: File = File(FileUtils.getPath(this, imageUri))
            val inputStream: InputStream = FileInputStream(file)
            bitmap = BitmapFactory.decodeStream(inputStream)
            /*Glide.with(this)
                    .load(bitmap)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(photoView)*/
            image = file.toString()
            Log.e("CROP", "Image:: $image")
            ProgressDialog.getInstance().showProgress(this@UpdateProfileActivity)
            registerApi(this@UpdateProfileActivity, mobile, name, image)
        } catch (e: java.lang.Exception) {
            Toast.makeText(this, "Please select different profile picture.", Toast.LENGTH_SHORT)
                    .show()
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA && resultCode == RESULT_OK) {
            val uri = Uri.parse(image)
            openCropActivity(uri, uri)
            /*CropImage.activity(uri)
                    .setAllowFlipping(false)
                    .setCropMenuCropButtonTitle("Done")
                    .start(this@RegisterActivity)*/
        }  else if (requestCode == GALLERY && resultCode == RESULT_OK && data != null) {
            try {
                val sourceUri = data.data
                val file = createImageFile()
                val destinationUri = Uri.fromFile(file)
                openCropActivity(sourceUri!!, destinationUri)
                /*CropImage.activity(sourceUri)
                        .setAllowFlipping(false)
                        .setCropMenuCropButtonTitle("Done")
                        .start(this@RegisterActivity)*/
            } catch (e: Exception) {
                Toast.makeText(this, "Please select another image", Toast.LENGTH_SHORT).show()
            }
        } /*else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK) {
                val resultUri = result.uri

                filee = resultUri.toFile()
                image = result.uri.toString()
                Log.e("CROP", "Image:: " + image)
                img_profile.setImageURI(resultUri)
                img_profile.setBorderColor(R.color.orange)
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
            }
        }*/ else if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
            if (data != null) {
                val uri: Uri? = UCrop.getOutput(data)
                showImage(uri!!)
            }
        }
    }
    private fun registerApi(context: Context, mobileno: String, username: String, image: String) {
        var loginObj = JsonObject()
        val jsonObject = JSONObject()
        try {
            jsonObject.put("mobileNumber", mobileno)
            jsonObject.put("fullName", username)
            val jsonParser = JsonParser()
            loginObj = jsonParser.parse(jsonObject.toString()) as JsonObject
            //print parameter
            Log.e("LOGINJSON:", " $loginObj")
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        RetrofitCallbacks.getInstace().ApiCallbacksProfile(
            context, "apna/updateProfilePic", loginObj.toString(),
            File(image), "updatepic"
        )
    }

    override fun successCallBack(body: String?, from: String?) {
        var jsonObject: JSONObject? = null
        try {
            jsonObject = JSONObject(body)
            if (body != null) {
                if (jsonObject.getString("response").equals("3")) {
                    try {
                        trimCache(this@UpdateProfileActivity)
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }
                    ImageLoader.getInstance().clearDiskCache()
                    ImageLoader.getInstance().clearMemoryCache()
                    if (bitmap != null) {
                        Glide.with(this)
                                .load(bitmap)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true)
                                .into(photoView)
                    }else{
                        UniversalImageLoader.setImage(
                                profilepic.substring(1),
                                photoView,
                                null,
                                ServerApiCollection.BASE_URL1
                        )
                    }
                    /*UniversalImageLoader.setImage(
                        profilepic.substring(1),
                        photoView,
                        null,
                        ServerApiCollection.BASE_URL1
                    )*/
                    ProgressDialog.getInstance().hideProgress()
                } else {
                    Toast.makeText(this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show()
                    ProgressDialog.getInstance().hideProgress()
                }
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }

    }

    override fun successdelete(body: String?, from: String?, notificationID: String?) {

    }

    override fun failureCallBack(failureMsg: String?) {
        ProgressDialog.getInstance().hideProgress()
        Toast.makeText(this, failureMsg, Toast.LENGTH_SHORT).show()
    }

    private fun trimCache(context: Context) {
        Log.e("cacheee", " ")
        try {
            val dir = context.cacheDir
            deleteDir(dir)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    private fun deleteDir(dir: File?): Boolean {
        return if (dir != null && dir.isDirectory) {
            val children = dir.list()
            for (i in children.indices) {
                val success = deleteDir(File(dir, children[i]))
                if (!success) {
                    return false
                }
            }
            dir.delete()
        } else {
            false
        }
    }
}