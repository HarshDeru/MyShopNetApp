package com.example.myshopnetapp.Activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.myshopnetapp.DatabaseFirestore.FirestoreClass
import com.example.myshopnetapp.Model.User
import com.example.myshopnetapp.R
import com.example.myshopnetapp.Utilities.Constants
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : BaseActivity(), View.OnClickListener {

    private lateinit var textView_register: TextView
    private lateinit var textView_ForgotPassword: TextView
    private lateinit var editText_email: EditText
    private lateinit var editText_password: EditText
    private lateinit var button_login: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        supportActionBar?.hide()


        //Hide the Top unnecessary Status Bar
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        editText_email = findViewById(R.id.editText_email)
        editText_password = findViewById(R.id.editText_password)
        button_login = findViewById(R.id.button_Login)
        textView_ForgotPassword = findViewById(R.id.textView_forgotPassword)
        textView_register = findViewById(R.id.textView_register)


        textView_ForgotPassword.setOnClickListener(this)
        button_login.setOnClickListener(this)
        textView_register.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.textView_forgotPassword -> {

                    val intent = Intent(this@LoginActivity, ForgotPasswordActivity::class.java)
                    startActivity(intent)
                }
                R.id.button_Login -> {
                    //Start
                    loginUsers()
                    //End
                }
                R.id.textView_register -> {
                    //Opens the Register page when the user clicks on Register text.
                    val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }
    //check User details are entered or empty as well as displays error message
    private fun checkLoginUserDetails(): Boolean {

        return when {
            TextUtils.isEmpty(editText_email.text.toString().trim{ it <= ' ' }) -> {
                displayErrorSnackBar(resources.getString(R.string.error_message_email),true)
                false
            }
            TextUtils.isEmpty(editText_password.text.toString().trim{ it <= ' ' }) -> {
                displayErrorSnackBar(resources.getString(R.string.error_message_password), true)
                false
            }else -> {
                true
            }
        }
    }

    //validate user details from firebase and allow the user to login
    private fun loginUsers() {
        if (checkLoginUserDetails()) {

            //show ProgressBar
            displayProgressBar("")

            val emailID = editText_email.text.toString().trim{ it <= ' ' }
            val passwordUser = editText_password.text.toString().trim{ it <= ' ' }

            //get Firebase login details
            FirebaseAuth.getInstance().signInWithEmailAndPassword(emailID,passwordUser)
                .addOnCompleteListener{ task ->

                    //false means green message and true means red error message
                    if (task.isSuccessful) {

                        FirestoreClass().getCurrentUserDetails(this@LoginActivity)
                    } else {
                        hideProgressBar()
                        displayErrorSnackBar(task.exception!!.message.toString(), true)
                    }
                }
        }
    }
    fun userLoggedInSuccessfully(user:User){

        //Hide ProgressBar
        hideProgressBar()

        //sent the user to different activity depending on profileCompleted
        if (user.profileCompleted == 0){
            //Open profile activity if they profile is incomplete
            val intent = Intent (this@LoginActivity, ProfileActivity::class.java)
            intent.putExtra(Constants.USER_DETAILS,user)
            startActivity(intent)
        }
        else{
            //Open main activity if the profile is completed
            startActivity(Intent(this@LoginActivity, NavigationActivity::class.java))
        }
        finish()
    }

}