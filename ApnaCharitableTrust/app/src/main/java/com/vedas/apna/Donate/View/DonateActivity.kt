package com.vedas.apna.Donate.View

import android.Manifest
import android.annotation.SuppressLint
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
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.vedas.apna.BuildConfig
import com.vedas.apna.Donate.Adapter.MultipleImagesPostAdapter
import com.vedas.apna.Donate.Presenter.DonatePresenter
import com.vedas.apna.Helper.UniversalImageLoader
import com.vedas.apna.Home.View.HomeActivity
import com.vedas.apna.R
import com.vedas.apna.ServerConnections.RetrofitCallbacks
import com.vedas.apna.ServerConnections.ServerApiCollection
import com.wang.avi.AVLoadingIndicatorView
import com.yalantis.ucrop.util.FileUtils
import de.hdodenhof.circleimageview.CircleImageView
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip
import org.json.JSONObject
import java.io.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class DonateActivity : AppCompatActivity(),IDonateView {
    private lateinit var img_back: ImageView
    private lateinit var rl_back: RelativeLayout
    private lateinit var radioGroup: RadioGroup
    private lateinit var radio_online: RadioButton
    private lateinit var radio_offline: RadioButton
    private lateinit var txt_donate: TextView
    private lateinit var spin: Spinner
    private lateinit var spin_deposit: Spinner
    private lateinit var mobile: EditText
    private lateinit var dName: EditText
    private lateinit var pan: EditText
    private lateinit var emailid: EditText
    private lateinit var address: EditText
    private lateinit var dAmount: EditText
    private lateinit var dMobile: EditText
    private lateinit var tran_date: EditText
    private lateinit var galleryPics: ImageView
    private lateinit var submit: Button
    private lateinit var cancel: Button
    private lateinit var avi: AVLoadingIndicatorView
    private lateinit var img_ref: CircleImageView
    private lateinit var img_next: ImageView

    private lateinit var li_deposit: LinearLayout
    private lateinit var recepitupload: LinearLayout
    private lateinit var li_trans: LinearLayout
    private lateinit var picRecycler: RecyclerView
    private var donationspent = ArrayList<String>()
    private var depositlist = ArrayList<String>()
    private val GALLERY = 1
    private val CAMERA = 2
    var image = ""
    private lateinit var bitmap: Bitmap
    private var uriList: ArrayList<Uri> = ArrayList()
    private var files: ArrayList<File> = ArrayList()
    lateinit var donatePresenter: DonatePresenter
    private var radio_str: String = "Online"

    lateinit var pref: SharedPreferences
    var mobileno: String = ""
    var profilepic: String = ""
    var name: String = ""
    var accesstoken: String = ""
    var email: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donate)
        init()
    }

    private fun init() {
        pref = getSharedPreferences("LoginPref", 0)
        accesstoken = pref.getString("accesstoken", null).toString()
        name = pref.getString("name", null).toString()
        profilepic = pref.getString("profilepic", null).toString()
        mobileno = pref.getString("mobileNumber", null).toString()
        email = pref.getString("userEmail", null).toString()

        img_back = findViewById(R.id.img_back)
        rl_back = findViewById(R.id.rl_back)
        radioGroup = findViewById(R.id.radioGroup)
        radio_online = findViewById(R.id.radio_online)
        radio_offline = findViewById(R.id.radio_offline)
        mobile = findViewById(R.id.mobile)
        dName = findViewById(R.id.dName)
        pan = findViewById(R.id.pan)
        avi = findViewById(R.id.avi)
        img_ref = findViewById(R.id.img_ref)
        submit = findViewById(R.id.submit)
        cancel = findViewById(R.id.cancel)
        galleryPics = findViewById(R.id.galleryPics)
        tran_date = findViewById(R.id.tran_date)
        dAmount = findViewById(R.id.dAmount)
        dMobile = findViewById(R.id.dMobile)
        address = findViewById(R.id.address)
        emailid = findViewById(R.id.emailid)
        picRecycler = findViewById(R.id.picRecycler)
        txt_donate = findViewById(R.id.txt_donate)
        li_deposit = findViewById(R.id.li_deposit)
        recepitupload = findViewById(R.id.recepitupload)
        li_trans = findViewById(R.id.li_trans)
        spin = findViewById(R.id.spin)
        img_next = findViewById(R.id.img_next)
        spin_deposit = findViewById(R.id.spin_deposit)

        UIFunctionalities()
    }

    @SuppressLint("SimpleDateFormat")
    private fun UIFunctionalities() {
        rl_back.setOnClickListener(rl_backclick)
        img_back.setOnClickListener(rl_backclick)
        cancel.setOnClickListener(rl_backclick)
        galleryPics.setOnClickListener(galleryPicsclick)
        txt_donate.setOnClickListener(txt_donateclick)
        submit.setOnClickListener(submitclick)
        img_next.setOnClickListener(img_nextbtnclick)
        radioGroup.setOnCheckedChangeListener(radioclick)
        mobile.addTextChangedListener(edituser)

        checkinguserlogin()
        donationspentfun()
        depositlistfun()
        val df = SimpleDateFormat("dd MMM yyyy")
        val date: String = df.format(Date())
        tran_date.setText(date)
        uriList = ArrayList()
        files = ArrayList()
        picRecycler.layoutManager = GridLayoutManager(this@DonateActivity, 3)
        val multipleImagesAdapter = MultipleImagesPostAdapter(
                applicationContext, uriList, files, galleryPics
        )
        picRecycler.adapter = multipleImagesAdapter
        if (files.size != 0) {
            Log.e("jdshfj", "fkjfkj")
            if (uriList.size == 3) {
                galleryPics.visibility = View.GONE
            } else {
                galleryPics.visibility = View.VISIBLE
            }
        }
        donatePresenter = DonatePresenter()
        donatePresenter.DonatePresenter(this, mobileno)
        RetrofitCallbacks.getInstace().initializeServerInterface(donatePresenter)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun checkinguserlogin() {
        if (mobileno != "null"){
            mobile.setText(mobileno)
            UniversalImageLoader.setImage(
                    profilepic.substring(1),
                    img_ref,
                    null,
                    ServerApiCollection.BASE_URL1
            )
        }else {
            mobile.setText(getString(R.string.Apna_number))
            Glide.with(this)
                    .load(ContextCompat.getDrawable(this@DonateActivity,R.drawable.logo_new))
                    .apply(RequestOptions.circleCropTransform())
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(img_ref)
        }
    }

    private val edituser: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
        override fun afterTextChanged(editable: Editable) {
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        override fun onTextChanged(s: CharSequence, i: Int, i1: Int, i2: Int) {
            if (!TextUtils.isEmpty(s)) {
                if (s.length == 10) {
                    if (mobileno != "null"){
                        when {
                            mobile.text.toString() == mobileno -> {
                                img_next.visibility = View.GONE
                                UniversalImageLoader.setImage(
                                        profilepic.substring(1),
                                        img_ref,
                                        null,
                                        ServerApiCollection.BASE_URL1
                                )
                            }
                            mobile.text.toString() == "9533669922" -> {
                                img_next.visibility = View.GONE
                                Glide.with(this@DonateActivity)
                                        .load(ContextCompat.getDrawable(this@DonateActivity,R.drawable.logo_new))
                                        .apply(RequestOptions.circleCropTransform())
                                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                                        .skipMemoryCache(true)
                                        .into(img_ref)
                            }
                            else -> {
                                //img_next.setVisibility(View.VISIBLE)
                                avi.visibility = View.VISIBLE
                                avi.smoothToShow()
                                donatePresenter.dofetchrefnumber(this@DonateActivity, mobile.text.toString())
                            }
                        }
                    }else{
                        if (mobile.text.toString() != "9533669922"){
                            //img_next.setVisibility(View.VISIBLE)
                            avi.visibility = View.VISIBLE
                            avi.smoothToShow()
                            donatePresenter.dofetchrefnumber(this@DonateActivity, mobile.text.toString())
                        }else{
                            Glide.with(this@DonateActivity)
                                    .load(ContextCompat.getDrawable(this@DonateActivity,R.drawable.logo_new))
                                    .apply(RequestOptions.circleCropTransform())
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(img_ref)
                        }
                    }
                } else {
                    img_next.visibility = View.GONE
                }
            } else {
                avi.visibility = View.GONE
            }
        }
    }

    private val img_nextbtnclick = View.OnClickListener {
        img_next.visibility = View.GONE
        avi.visibility = View.VISIBLE
        avi.smoothToShow()
        donatePresenter.dofetchrefnumber(this@DonateActivity, mobile.text.toString())
    }

    private fun depositlistfun() {
        depositlist.add("Select any one")
        depositlist.add("CASH")
        depositlist.add("NEFT/RTGS/IMPS")
        depositlist.add("CHEQUE")

        val aa: ArrayAdapter<String> = ArrayAdapter<String>(
                this@DonateActivity,
                R.layout.simple_spinner_item,
                depositlist
        )
        aa.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        spin_deposit.adapter = aa
        spin_deposit.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                    parentView: AdapterView<*>?,
                    selectedItemView: View,
                    position: Int,
                    id: Long
            ) {
                // your code here
                Log.e("spin", " " + spin_deposit.selectedItem.toString())
                /*if (spin.selectedItemPosition == 5) {
                    spin_check = true
                    spin.visibility = View.GONE
                } else {
                    spin_check = false
                    spin.visibility = View.VISIBLE
                }*/
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {}
        }
    }

    private fun donationspentfun() {
        donationspent.add("Select any one")
        donationspent.add("Health")
        donationspent.add("Education")
        donationspent.add("Rural Development")
        donationspent.add("Sports")
        donationspent.add("Culture")
        donationspent.add("Social Service")
        val aa: ArrayAdapter<String> = ArrayAdapter<String>(
                this@DonateActivity,
                R.layout.simple_spinner_item,
                donationspent
        )
        aa.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        spin.adapter = aa
        spin.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                    parentView: AdapterView<*>?,
                    selectedItemView: View,
                    position: Int,
                    id: Long
            ) {
                // your code here
                Log.e("spin", " " + spin.selectedItem.toString())
                /*if (spin.selectedItemPosition == 5) {
                    spin_check = true
                    spin.visibility = View.GONE
                } else {
                    spin_check = false
                    spin.visibility = View.VISIBLE
                }*/
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {}
        }
    }

    private val submitclick = View.OnClickListener {
        val imm = (getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager)
        if (imm.isAcceptingText) {
            Log.e("shown", "")
            imm.hideSoftInputFromWindow(
                    submit.windowToken,
                    InputMethodManager.RESULT_UNCHANGED_SHOWN
            )
        } else {
            Log.e("unshown", "")
        }
        if (radio_str == "Online") {
            donatePresenter.dosubmit(
                    this,
                    mobile,
                    dName,
                    dMobile,
                    pan,
                    emailid,
                    address,
                    dAmount,
                    tran_date,
                    spin_deposit,
                    spin,
                    files,
                    "online"
            )
        } else {
            donatePresenter.dosubmit(
                    this,
                    mobile,
                    dName,
                    dMobile,
                    pan,
                    emailid,
                    address,
                    dAmount,
                    tran_date,
                    spin_deposit,
                    spin,
                    files,
                    "offline"
            )
        }
    }

    private val rl_backclick = View.OnClickListener {
        onBackPressed()
    }

    private val radioclick = RadioGroup.OnCheckedChangeListener { group, checkedId ->
        val radio: RadioButton = group.findViewById(checkedId)
        if (radio.text.toString() == "Online") {
            li_deposit.visibility = View.GONE
            recepitupload.visibility = View.GONE
            li_trans.visibility = View.GONE
        } else {
            li_deposit.visibility = View.VISIBLE
            recepitupload.visibility = View.VISIBLE
            li_trans.visibility = View.VISIBLE
        }
        radio_str = radio.text.toString()
        Log.e("selectedtext-->", radio.text.toString())
    }

    private val txt_donateclick = View.OnClickListener {
        SimpleTooltip.Builder(this)
            .anchorView(txt_donate)
            .text("Donation Amount should not exceed Rs.2000 only.")
            .textColor(ContextCompat.getColor(this@DonateActivity,R.color.white))
            .gravity(Gravity.END)
            .setWidth(280)
            .margin(10F)
            .animated(true)
            //.transparentOverlay(false)
            .dismissOnOutsideTouch(true)
            .dismissOnInsideTouch(false)
            .arrowHeight(10F)
            .arrowWidth(20F)
            .build()
            .show()
    }

    private val galleryPicsclick = View.OnClickListener {
        if (uriList.size <= 3) {
            requestMultiplePermissions()
        } else {
            Toast.makeText(applicationContext, "You can add only 3 images", Toast.LENGTH_SHORT)
                .show()
        }
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
                                        this@DonateActivity,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                                ) != PackageManager.PERMISSION_GRANTED &&
                                ContextCompat.checkSelfPermission(
                                        this@DonateActivity,
                                        Manifest.permission.CAMERA
                                ) != PackageManager.PERMISSION_GRANTED &&
                                ContextCompat.checkSelfPermission(
                                        this@DonateActivity,
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
        val dialog = BottomSheetDialog(this@DonateActivity, R.style.BottomSheetDialogTheme)
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
        val galleryIntent =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).setType(
                    "image/*"
            )
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
            FileProvider.getUriForFile(
                    this,
                    BuildConfig.APPLICATION_ID + ".provider",
                    file
            ) else Uri.fromFile(
                file
        ) // 3
        pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri) // 4
        startActivityForResult(pictureIntent, CAMERA)
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        @SuppressLint("SimpleDateFormat") val timeStamp =
            SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA && resultCode == RESULT_OK) {
            val uri = Uri.parse(image)
            showImage(uri!!)
        } else if (requestCode == GALLERY && resultCode == RESULT_OK && data != null) {
            try {
                val sourceUri = data.data
                val file = createImageFile()
                //val destinationUri = Uri.fromFile(file)
                showImage(sourceUri!!)
            } catch (e: Exception) {
                Toast.makeText(this, "Please select another image", Toast.LENGTH_SHORT).show()
            }
        }else if (requestCode == 3 && data != null) {
            val bundle: Bundle = data.extras!!
            Log.e("paymentResponse", "" + bundle)
            for (key in bundle.keySet()) {
                Log.e(
                        "Transatction",
                        key + " : " + if (bundle[key] != null) bundle[key] else "NULL"
                )

            }
            Log.e("Transatction1", " data:: " + data.getStringExtra("nativeSdkForMerchantMessage"))
            Log.e("Transatction2", " data response - " + data.getStringExtra("response"))

            val answer = JSONObject(data.getStringExtra("response").toString())
            val txnStatus: String = answer.getString("STATUS").toString()
            val txnId: String = answer.getString("TXNID").toString()
            //val orderId: String = answer.getString("ORDERID").toString()
            Log.e("presenter1234", "donate:: $txnStatus")
            if (txnStatus == "TXN_SUCCESS") {
                donatePresenter.donationConfirmation(
                        this@DonateActivity,
                        txnStatus,
                        txnId,
                )
            }
/*
 data response - {"BANKNAME":"WALLET","BANKTXNID":"1394221115",
 "CHECKSUMHASH":"7jRCFIk6eRmrep+IhnmQrlrL43KSCSXrmM+VHP5pH0ekXaaxjt3MEgd1N9mLtWyu4VwpWexHOILCTAhybOo5EVDmAEV33rg2VAS/p0PXdk\u003d",
 "CURRENCY":"INR","GATEWAYNAME":"WALLET","MID":"EAcP3138556","ORDERID":"100620202152",
 "PAYMENTMODE":"PPI","RESPCODE":"01","RESPMSG":"Txn Success","STATUS":"TXN_SUCCESS",
 "TXNAMOUNT":"2.00","TXNDATE":"2020-06-10 16:57:45.0","TXNID":"2020061011121280011018328631290118"}
  */
            /*Toast.makeText(
                this, data.getStringExtra("nativeSdkForMerchantMessage")
                        + data.getStringExtra("response"), Toast.LENGTH_SHORT
            ).show()*/
        }else{
            Log.e("Donate", " payment failed")
        }
    }

    private fun showImage(imageUri: Uri) {
        try {
            val filee: File
            val file = File(FileUtils.getPath(this, imageUri))
            val inputStream: InputStream = FileInputStream(file)
            bitmap = BitmapFactory.decodeStream(inputStream)
            val f: File? = null
            filee = createImageFileFromBitmap(bitmap, f)!!
            image = filee.toString()
            //files.add(file)
            //image = file.toString()

            if (uriList.size < 3) {
                files.add(filee)
                uriList.add(Uri.fromFile(filee))
                if (uriList.size == 3) {
                    galleryPics.visibility = View.GONE
                } else {
                    galleryPics.visibility = View.VISIBLE
                }
                val multipleImagesAdapter = MultipleImagesPostAdapter(
                        applicationContext, uriList, files, galleryPics
                )
                picRecycler.adapter = multipleImagesAdapter
            } else {
                Toast.makeText(applicationContext, "You can add only 3 images", Toast.LENGTH_SHORT)
                    .show()
            }
            Log.e("CROP", "Image:: $image\n Files::$filee")
        } catch (e: Exception) {
            Toast.makeText(this, "Please select different profile picture.", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun createImageFileFromBitmap(bitmap: Bitmap, ff: File?): File? {
        var f: File? = ff
        try {
            val outStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream)
            f = createImageFile()
            return try {
                val fo = FileOutputStream(f)
                fo.write(outStream.toByteArray())
                fo.flush()
                fo.close()
                f
            } catch (e: IOException) {
                Log.w("TAG", "Error saving image file: " + e.message)
                null
            }
        } catch (e: IOException) {
// TODO Auto-generated catch block
            e.printStackTrace()
            return null
        }
    }

    override fun ToastMsg(toastmsg: String) {
        Toast.makeText(this, toastmsg, Toast.LENGTH_SHORT).show()
    }

    override fun donateSuccess(success: String) {
        finishAffinity()
        startActivity(Intent(this@DonateActivity, HomeActivity::class.java))
    }

    override fun donatefail() {
        avi.hide()
        img_next.visibility = View.GONE
        avi.visibility = View.GONE
        mobile.clearFocus()
    }

    override fun offlineSuccess() {

    }

    override fun fetchrefnumber(refnumber: String) {
        avi.hide()
        img_next.visibility = View.GONE
        avi.visibility = View.GONE
        mobile.clearFocus()
        Glide.with(this@DonateActivity)
            .load(ServerApiCollection.BASE_URL1 + refnumber.substring(1))
            .apply(RequestOptions.circleCropTransform())
            .into(img_ref)
        dName.requestFocus()
    }

    override fun paytmSuccess() {

    }

}