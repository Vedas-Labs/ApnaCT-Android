package com.vedas.apna.Register.View

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.vedas.apna.BuildConfig
import com.vedas.apna.Login.View.LoginActivity
import com.vedas.apna.R
import com.vedas.apna.Register.Presenter.RegisterPresenter
import com.vedas.apna.ServerConnections.RetrofitCallbacks
import com.vedas.apna.ServerConnections.ServerApiCollection
import com.wang.avi.AVLoadingIndicatorView
import com.yalantis.ucrop.UCrop
import com.yalantis.ucrop.util.FileUtils
import de.hdodenhof.circleimageview.CircleImageView
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*


class RegisterActivity : AppCompatActivity(), RegisterView {
    private lateinit var ed_name: EditText
    private lateinit var ed_pswd : EditText
    private lateinit var ed_set_pswd : EditText
    private lateinit var ed_refnumber : EditText
    private lateinit var ed_email : EditText
    private lateinit var ed_phone : EditText
    private lateinit var regbtn: Button
    private lateinit var btn_cancel: Button
    private lateinit var img_back: ImageView
    private lateinit var img_next: ImageView
    private lateinit var img_pswd_eye: ImageView
    private lateinit var img_setpswd_eye: ImageView
    private lateinit var img_ref: CircleImageView
    private lateinit var iv_cam: ImageView
    private lateinit var avi: AVLoadingIndicatorView
    private lateinit var img_profile: CircleImageView
    private lateinit var rl_back: RelativeLayout
    private lateinit var rl_eye: RelativeLayout
    private lateinit var rl_set_eye: RelativeLayout
    private lateinit var registerPresenter: RegisterPresenter
    private val GALLERY = 1
    private val CAMERA = 2
    var image = ""
    private var eye1:Boolean = false
    private var eye2:Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        init()
    }

    private fun init() {
        ed_name = findViewById(R.id.ed_name)
        ed_pswd = findViewById(R.id.ed_pswd)
        ed_refnumber = findViewById(R.id.ed_refnumber)
        ed_email = findViewById(R.id.ed_email)
        avi = findViewById(R.id.avi)
        img_next = findViewById(R.id.img_next)
        ed_phone = findViewById(R.id.ed_phone)
        img_setpswd_eye = findViewById(R.id.img_setpswd_eye)
        img_pswd_eye = findViewById(R.id.img_pswd_eye)
        ed_set_pswd = findViewById(R.id.ed_set_pswd)
        regbtn = findViewById(R.id.btn_register)
        rl_set_eye = findViewById(R.id.rl_set_eye)
        rl_eye = findViewById(R.id.rl_eye)
        img_back = findViewById(R.id.img_back)
        iv_cam = findViewById(R.id.iv_cam)
        img_ref = findViewById(R.id.img_ref)
        rl_back = findViewById(R.id.rl_back)
        img_profile = findViewById(R.id.img_profile)
        btn_cancel = findViewById(R.id.btn_cancel)

        registerPresenter = RegisterPresenter()
        registerPresenter.RegisterPresenter(this)
        RetrofitCallbacks.getInstace().initializeServerInterface(registerPresenter)
        regbtn.setOnClickListener(regbtnclick)
        img_next.setOnClickListener(img_nextbtnclick)
        btn_cancel.setOnClickListener(cancelbtnclick)
        rl_back.setOnClickListener(rl_backclick)
        img_back.setOnClickListener(rl_backclick)
        iv_cam.setOnClickListener(iv_camclick)
        rl_eye.setOnClickListener(rl_eyeclick)
        rl_set_eye.setOnClickListener(rl_set_eyeclick)
        iv_cam.setOnClickListener(iv_camclick)
        ed_refnumber.addTextChangedListener(edituser)
        ed_refnumber.setText(getString(R.string.Apna_number))
        img_ref.setImageResource(R.drawable.logo_new)
        /*ed_refnumber.setFocusable(false)
        ed_refnumber.setClickable(false)*/
    }
    private val edituser: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
        override fun afterTextChanged(editable: Editable) {
        }
        override fun onTextChanged(s: CharSequence, i: Int, i1: Int, i2: Int) {
            if (!TextUtils.isEmpty(s)) {
                if(s.length == 10  && ed_refnumber.text.toString() != "9533669922"){
                    //ToastMsg("10 digits. ok!!")
                    //img_next.setVisibility(View.VISIBLE)
                    avi.visibility = View.VISIBLE
                    avi.smoothToShow()
                    registerPresenter.dofetchrefnumber(this@RegisterActivity, ed_refnumber.text.toString())
                    //registerPresenter.dofetchrefnumber(this@RegisterActivity,s.toString(),avi)
                }else{
                    if (ed_refnumber.text.toString() == "9533669922"){
                        img_ref.setImageResource(R.drawable.logo_new)
                    }
                    img_next.visibility = View.GONE
                }
            } else {
                avi.visibility = View.GONE
            }
        }
    }
    private val regbtnclick = View.OnClickListener {
        val imm = (getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager)
        if (imm.isAcceptingText) {
            Log.e("shown", "")
            imm.hideSoftInputFromWindow(
                    regbtn.windowToken,
                    InputMethodManager.RESULT_UNCHANGED_SHOWN
            )
        } else {
            Log.e("unshown", "")
        }
        registerPresenter.doRegister(
                this@RegisterActivity, ed_refnumber, ed_name, ed_email,
                ed_phone, ed_pswd, ed_set_pswd, image
        )
    }
    private val cancelbtnclick = View.OnClickListener {
        //finish()
        onBackPressed()
        //startActivity(Intent(this@RegisterActivity, HomeActivity::class.java))
    }
    private val rl_backclick = View.OnClickListener {
        onBackPressed()
    }
    @SuppressLint("UseCompatLoadingForDrawables")
    private val rl_eyeclick = View.OnClickListener {
        if (!eye1){
            ed_pswd.post { ed_pswd.setSelection(ed_pswd.length()) }
            img_pswd_eye.setImageDrawable(resources.getDrawable(R.drawable.eye_on))
            ed_pswd.transformationMethod = HideReturnsTransformationMethod.getInstance()
            eye1=true
        }else{
            ed_pswd.post { ed_pswd.setSelection(ed_pswd.length()) }
            img_pswd_eye.setImageDrawable(resources.getDrawable(R.drawable.eye_off))
            ed_pswd.transformationMethod = PasswordTransformationMethod.getInstance()
            eye1=false
        }
    }
    @SuppressLint("UseCompatLoadingForDrawables")
    private val rl_set_eyeclick = View.OnClickListener {
        if (!eye2){
            ed_set_pswd.post { ed_set_pswd.setSelection(ed_set_pswd.length()) }
            img_setpswd_eye.setImageDrawable(ContextCompat.getDrawable(this@RegisterActivity,R.drawable.eye_on))
            ed_set_pswd.transformationMethod = HideReturnsTransformationMethod.getInstance()
            eye2=true
        }else{
            ed_set_pswd.post { ed_set_pswd.setSelection(ed_set_pswd.length()) }
            img_setpswd_eye.setImageDrawable(ContextCompat.getDrawable(this@RegisterActivity,R.drawable.eye_off))
            ed_set_pswd.transformationMethod = PasswordTransformationMethod.getInstance()
            eye2=false
        }
    }
    private val iv_camclick = View.OnClickListener {
        requestMultiplePermissions()
    }
    private val img_nextbtnclick = View.OnClickListener {
        img_next.visibility = View.GONE
        avi.visibility = View.VISIBLE
        avi.smoothToShow()
        registerPresenter.dofetchrefnumber(this@RegisterActivity, ed_refnumber.text.toString())
    }
    /*fun SuccessAlertDialog() {
        val dialog = Dialog(this@RegisterActivity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setContentView(R.layout.success_popup)
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        val btn_unfriend = dialog.findViewById<RelativeLayout>(R.id.rl_cancel)
        val txt_username = dialog.findViewById<TextView>(R.id.txt_username)
        txt_username.text = "Registration Successfully!!"
        //txt_username.setOnClickListener { dialog.dismiss() }
        btn_unfriend.setOnClickListener {
            dialog.dismiss()
            finishAffinity()
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
        }
        dialog.show()
    }*/

    override fun registerSuccess(success: String) {
        //SuccessAlertDialog()
        ToastMsg(success)
        finishAffinity()
        startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
    }

    override fun registerfail() {
        avi.hide()
        img_next.visibility = View.GONE
        avi.visibility = View.GONE
        ed_refnumber.clearFocus()
    }

    override fun fetchrefnumber(refnumber: String) {
        avi.hide()
        img_next.visibility = View.GONE
        avi.visibility = View.GONE
        ed_refnumber.clearFocus()
        Glide.with(this@RegisterActivity)
                .load(ServerApiCollection.BASE_URL1 + refnumber.substring(1))
                .apply(RequestOptions.circleCropTransform())
                .into(img_ref)
        //ed_refnumber.setText(refnumber)
        ed_name.requestFocus()
    }

    override fun ToastMsg(toastmsg: String) {
        Toast.makeText(this, toastmsg, Toast.LENGTH_SHORT).show()
    }

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
                                            this@RegisterActivity,
                                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                                    ) != PackageManager.PERMISSION_GRANTED &&
                                    ContextCompat.checkSelfPermission(
                                            this@RegisterActivity,
                                            Manifest.permission.CAMERA
                                    ) != PackageManager.PERMISSION_GRANTED &&
                                    ContextCompat.checkSelfPermission(
                                            this@RegisterActivity,
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
        val dialog = BottomSheetDialog(this@RegisterActivity, R.style.BottomSheetDialogTheme)
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
            .start(this@RegisterActivity)
    }
    private fun showImage(imageUri: Uri) {
        try {
            val file = File(FileUtils.getPath(this, imageUri))
            val inputStream: InputStream = FileInputStream(file)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            Glide.with(this)
                .load(bitmap)
                .apply(RequestOptions.circleCropTransform())
                .into(img_profile)
            image = file.toString()
            Log.e("CROP", "Image:: $image")
            img_profile.borderColor = ContextCompat.getColor(this@RegisterActivity,R.color.orange)
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
}