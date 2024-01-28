package com.example.myshopnetapp.Utilities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap

object Constants {
    //will allow me to use the constants throughout whole application to make the program look clean
    //and not to get confused with word type.
    const val USERS: String = "users"
    const val MYSHOPNET_PREFERENCES: String = "MyShopNetPrefs"
    const val LOGGED_USERNAME:String = "logged_in_username"
    const val USER_DETAILS: String = "user_details"
    const val READ_EXTERNAL_STORAGE_PERMISSION = 2
    const val PICK_USER_IMAGE_REQUEST = 1
    const val FIRST_NAME: String="firstName"
    const val LAST_NAME: String="lastName"
    const val IMAGE:String = "image"
    const val MOBILE: String = "mobile"
    const val GENDER: String = "gender"
    const val MALE: String = "male"
    const val FEMALE: String = "female"
    const val PROFILE_COMPLETED :String = "profileCompleted"
    const val PROFILE_USER_IMAGE: String = "Use_Profile_Image"

    //image chooser
    fun showImagePicker(activity:Activity){
        //intent that launches gallery of the user's phone
        val photoIntent = Intent(Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        //launches the selected code of phone storage using the constant code
        activity.startActivityForResult(photoIntent, PICK_USER_IMAGE_REQUEST)
    }

    fun getFileExtension(activity: Activity, uri: Uri?): String? {
        //code to get the file extensions from user device
        //we passing the uri which is the link that the image has on the device
        //example- c:/user/harsh/download/profile.jpg
        return MimeTypeMap.getSingleton()
            .getExtensionFromMimeType(activity.contentResolver.getType(uri!!))

    }





}