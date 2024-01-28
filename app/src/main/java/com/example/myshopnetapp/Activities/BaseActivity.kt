package com.example.myshopnetapp.Activities

import android.app.Dialog
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.myshopnetapp.R
import com.google.android.material.snackbar.Snackbar

@Suppress("DEPRECATION")
open class BaseActivity : AppCompatActivity() {

    private lateinit var progressBar: Dialog
    private var clickDoubleBackToExitPressedOnce = false

    //displays error message
    fun displayErrorSnackBar(message:String, errorMessage: Boolean){
        val snackBar = Snackbar.make(findViewById(android.R.id.content),message, Snackbar.LENGTH_LONG)
        val snackBarView = snackBar.view

        if(errorMessage){
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(this@BaseActivity, R.color.colorSnackBarError)
            )
        }else{
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(this@BaseActivity, R.color.colorSnackBarSuccess)
            )
        }
        snackBar.show()
    }

    fun displayProgressBar(text: String){
        progressBar = Dialog(this)

        //set the screen content from activity_progress.
        progressBar.setContentView(R.layout.activity_progress)

        //Cannot be disappeared by clicking
        progressBar.setCancelable(false)
        progressBar.setCanceledOnTouchOutside(true)

        //Display on screen
        progressBar.show()
    }

    fun hideProgressBar(){
        //Remove progressBar from screen
        progressBar.dismiss()
    }

    fun clickDoubleBackToExit(){
        if (clickDoubleBackToExitPressedOnce){
            super.onBackPressed()
            return
        }
        this.clickDoubleBackToExitPressedOnce = true
        Toast.makeText(this, resources.getString(R.string.please_click_again), Toast.LENGTH_SHORT)
            .show()

        Handler().postDelayed({ clickDoubleBackToExitPressedOnce = false}, 2000)

    }


}