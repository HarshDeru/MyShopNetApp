package com.example.myshopnetapp.Activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.myshopnetapp.DatabaseFirestore.FirestoreClass
import com.example.myshopnetapp.Model.User
import com.example.myshopnetapp.R
import com.example.myshopnetapp.Utilities.Constants
import com.example.myshopnetapp.Utilities.GlideImageLoader
import kotlinx.android.synthetic.main.activity_profile.*
import java.io.IOException

class ProfileActivity : BaseActivity(), View.OnClickListener {

    private lateinit var editText_firstName: EditText
    private lateinit var editText_lastName: EditText
    private lateinit var editText_emailID: EditText
    private lateinit var pUserDetails: User
    private var pUserProfileImageURI: String =""
    private var pSelectedImageFileUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        supportActionBar?.hide()

        //checks if the detail are existed and then getParcelableExtra
        if (intent.hasExtra(Constants.USER_DETAILS)) {
            pUserDetails = intent.getParcelableExtra(Constants.USER_DETAILS)!!
        }


        editText_firstName = findViewById(R.id.editText_firstName)
        editText_firstName.setText(pUserDetails.firstName)


        editText_lastName = findViewById(R.id.editText_lastName)
        editText_lastName.setText(pUserDetails.lastName)
        //the user won't be able to change their emailID
        editText_emailID = findViewById(R.id.editText_profileEmail)
        editText_emailID.isEnabled = false
        editText_emailID.setText(pUserDetails.email)

        if(pUserDetails.profileCompleted ==0){
            textView_titleUser.text = resources.getString(R.string.title_profileComplete)

            //get the details from users object
            editText_firstName.isEnabled = false

            editText_lastName.isEnabled = false

        }else{
            textView_titleUser.text = resources.getString(R.string.title_editProfile)
            GlideImageLoader(this).loadProfileImage(pUserDetails.image, imageView_userProfile)

            if(pUserDetails.mobile != 0L){
                editText_mobileNumber.setText(pUserDetails.mobile.toString())
            }
            if(pUserDetails.gender == Constants.MALE){
                radioButton_male.isChecked = true
            } else{
                radioButton_female.isChecked = true
            }
        }

        //onClickListener for image and profile save button
        imageView_userProfile.setOnClickListener(this@ProfileActivity)
        button_profileSave.setOnClickListener(this@ProfileActivity)

    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.imageView_userProfile -> {
                    // the system will check if the permission to access gallery is provided or not.
                    //check if the permission is already granted or not to add images.
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        )
                        == PackageManager.PERMISSION_GRANTED
                    ) {
                        //opens and allows the user to select image if the permission is already granted
                        Constants.showImagePicker(this)
                    } else {
                        //request for permission if not provided.
                        ActivityCompat.requestPermissions(
                            this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            Constants.READ_EXTERNAL_STORAGE_PERMISSION
                        )
                    }
                }

                //check that no fields are left empty
                R.id.button_profileSave -> {

                    if (validateProfileUserDetails()) {
                        displayProgressBar(resources.getString(R.string.pleaseWait))

                        //check if the profile image is not empty
                        if (pSelectedImageFileUri != null)
                            //run the code is the image is not null and call the profileImageUpdate
                            FirestoreClass().uploadProfileImageToStorage(
                                this,
                                pSelectedImageFileUri
                            )
                        else{
                            //do this
                            profileImageUpdate()
                        }
                    }
                }
            }
        }
    }

    //provides the answer if the user granted the permission or not
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //display message if permission is granted
        if (requestCode == Constants.READ_EXTERNAL_STORAGE_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //if the user grants the permission than show image chooser
                Constants.showImagePicker(this)
            } else {
                //display message if permission is denied
                Toast.makeText(
                    this,
                    resources.getString(R.string.read_permission_denied),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    //Receive the result, check the if the user is able to add image or not.
    //URI is used to identify resources such as images, video etc. It stands for Uniform Resource Identifier.
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //if the result is ok then check if the request code granted and then process.
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.PICK_USER_IMAGE_REQUEST) {
                if (data != null) {
                    try {
                        //Uri of selected image
                        pSelectedImageFileUri = data.data!!
                        //displays the selected image
                        GlideImageLoader(this).loadProfileImage(pSelectedImageFileUri!!,imageView_userProfile)
                    } catch (e: IOException) {
                        e.printStackTrace()
                        //error message image selection failed.
                        Toast.makeText(
                            this@ProfileActivity,
                            resources.getString(R.string.image_failedSelection),
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
            }
        }
        else if (resultCode == Activity.RESULT_CANCELED) {
            //error message will be printed when the user cancel or closes the image selection.
            Log.e("Request Cancelled", "Profile image selection cancelled")
        }
    }

    //validate the user mobile number.
    private fun validateProfileUserDetails(): Boolean {
        return when {
            // show an error message if the mobile number edit text is empty
            TextUtils.isEmpty(editText_mobileNumber.text.toString().trim {it <= ' '}) -> {
                displayErrorSnackBar(resources.getString(R.string.error_message_mobile_number), true)
                false
            }
            else -> {
                true
            }
        }
    }

    //userProfile has updated successfully
    fun profileUpdatedSuccessfully(){
        hideProgressBar()
        Toast.makeText(this@ProfileActivity,
            resources.getString(R.string.profile_updatedSuccessfully),
            Toast.LENGTH_SHORT).show()

        startActivity(Intent(this@ProfileActivity, NavigationActivity::class.java))
        finish()
    }

    fun imageUploadedSuccessfully(imageURL: String){
        pUserProfileImageURI = imageURL
        profileImageUpdate()
    }

    private fun profileImageUpdate(){
        //userHashMap is used to store mobile number and gender.
        val userHashMap = HashMap<String, Any>()
        val firstName = editText_firstName.text.toString().trim { it <= ' '}
        //check if the firstname is different than before and then add it to the user hashmap
        if(firstName != pUserDetails.firstName) {
            userHashMap[Constants.FIRST_NAME] = firstName
        }

        val lastName = editText_lastName.text.toString().trim { it <= ' '}
        //check if the lastname is different than before and then add it to the user hashmap
        if(lastName != pUserDetails.lastName) {
            userHashMap[Constants.LAST_NAME] = lastName
        }

        val mobileNumber = editText_mobileNumber.text.toString().trim { it <= ' ' }
        val gender = if (radioButton_male.isChecked) {
            Constants.MALE
        } else {
            Constants.FEMALE
        }

        //check if the profile is not empty and store it in the user hashmap
        if(pUserProfileImageURI.isNotEmpty()){
            userHashMap[Constants.IMAGE] = pUserProfileImageURI
        }
        //check if the mobile number is not empty and store it in the user hashmap only if changes are made
        if (mobileNumber.isNotEmpty() && mobileNumber != pUserDetails.mobile.toString()) {
            userHashMap[Constants.MOBILE] = mobileNumber.toLong()
        }

        //check if the gender is not empty and store it in the user hashmap only if changes are made
        if (gender.isNotEmpty() && gender != pUserDetails.gender) {
            userHashMap[Constants.GENDER] = gender
        }

        userHashMap[Constants.GENDER] = gender
        userHashMap[Constants.PROFILE_COMPLETED] = 1
        FirestoreClass().updateUserProfileDetails(this, userHashMap)
    }
}