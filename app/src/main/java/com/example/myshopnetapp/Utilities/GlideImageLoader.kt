package com.example.myshopnetapp.Utilities

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.myshopnetapp.R
import java.io.IOException

class GlideImageLoader (val context: Context){

    //Load the selected image in the imageview
    fun loadProfileImage (image: Any, imageview: ImageView) {
        try {
            Glide
                .with(context)
                //URI og the image
                .load((image))
                //scale type of the image
                .centerCrop()
                //if image is failed to load then display default image
                .placeholder(R.drawable.user_profile_demo)
                //view in which the image will be displayed
                .into(imageview)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}