package com.example.myshopnetapp.Activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.*
import com.example.myshopnetapp.DatabaseFirestore.FirestoreClass
import com.example.myshopnetapp.Model.User
import com.example.myshopnetapp.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class RegisterActivity : BaseActivity() {

    private lateinit var textView_login: TextView
    private lateinit var register_button: Button
    private lateinit var termsCondition : TextView
    private lateinit var back_button : TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

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
        //Do not repeat all the steps by clicking on back button
        textView_login = findViewById(R.id.tv_login)
        textView_login.setOnClickListener {
            onBackPressed()
        }

        register_button = findViewById(R.id.button_register)
        register_button.setOnClickListener {
            registerNewUser()
        }

        termsCondition= findViewById(R.id.textView_terms_condition)
        termsCondition.setOnClickListener {
            val intent = Intent(this@RegisterActivity, TermsConditionActivity::class.java)
            startActivity(intent)
        }

        back_button= findViewById(R.id.back_button)
        back_button.setOnClickListener {
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    /**
     * CheckRegisterDetails function will validate new registered users
     */
    private fun checkRegisterDetails(): Boolean {
        val editFirstName = findViewById<EditText>(R.id.edit_firstName)
        val editLastName = findViewById<EditText>(R.id.edit_lastName)
        val editEmail = findViewById<EditText>(R.id.edit_email)
        val editPassword = findViewById<EditText>(R.id.edit_password)
        val editConfirmPassword = findViewById<EditText>(R.id.edit_confirmPassword)
        val checkBoxTAndC = findViewById<CheckBox>(R.id.checkBox_terms_and_Condition)

        return when {
            TextUtils.isEmpty(editFirstName.text.toString().trim { it <= ' ' }) -> {
                displayErrorSnackBar(resources.getString(R.string.error_message_firstName), true)
                false
            }
            TextUtils.isEmpty(editLastName.text.toString().trim { it <= ' ' }) -> {
                displayErrorSnackBar(resources.getString(R.string.error_message_lastName), true)
                false
            }
            TextUtils.isEmpty(editEmail.text.toString().trim { it <= ' ' }) -> {
                displayErrorSnackBar(resources.getString(R.string.error_message_email), true)
                false
            }
            TextUtils.isEmpty(editPassword.text.toString().trim { it <= ' ' }) -> {
                displayErrorSnackBar(resources.getString(R.string.error_message_password), true)
                false
            }
            TextUtils.isEmpty(editConfirmPassword.text.toString().trim { it <= ' ' }) -> {
                displayErrorSnackBar(
                    resources.getString(R.string.error_message_confirmPassword),
                    true
                )
                false
            }
            editPassword.text.toString().trim { it <= ' ' } != editConfirmPassword.text.toString()
                .trim { it <= ' ' } -> {
                displayErrorSnackBar(
                    resources.getString(R.string.error_message_password_confirmPassword_mismatch),
                    true
                )
                false
            }
            !checkBoxTAndC.isChecked -> {
                displayErrorSnackBar(
                    resources.getString(R.string.error_message_termsAndCondition),
                    true
                )
                false
            }
            else -> {
                //displayErrorSnackBar(resources.getString(R.string.successfully_registered), false)
                true
            }
        }
    }

    private fun registerNewUser() {

        val editFirstName = findViewById<EditText>(R.id.edit_firstName)
        val editLastName = findViewById<EditText>(R.id.edit_lastName)
        val email = findViewById<EditText>(R.id.edit_email)
        val password = findViewById<EditText>(R.id.edit_password)

        //Check with checkRegisterDetails function if each entries are inputted correctly or not.
        //If the details are validated and true only then follow the rest code.
        if (checkRegisterDetails()) {

            //show progressBar
            displayProgressBar("")

            //Remove empty spaces
            val emailIDUser: String = email.text.toString().trim { it <= ' ' }
            val passwordUser: String = password.text.toString().trim { it <= ' ' }

            //create an instance and register new user using emailID and Password
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(emailIDUser, passwordUser)
                .addOnCompleteListener(
                    OnCompleteListener<AuthResult> { task ->

                        //If the registration is successfully done.
                        if (task.isSuccessful) {
                            // Register new user on firebase
                            val firebaseUser: FirebaseUser = task.result!!.user!!

                            //Add new user to Firestore
                            val user = User(
                                firebaseUser.uid,
                                editFirstName.text.toString().trim { it <= ' '},
                                editLastName.text.toString().trim { it <= ' '},
                                email.text.toString().trim { it <= ' '},
                            )
                            //calls the firestoreClass activity
                            FirestoreClass().userRegister(this@RegisterActivity, user)

                            //SignOut the user and sent the user back to login page
                            //FirebaseAuth.getInstance().signOut()
                            //finish()

                        } else {
                            //Show an error message if registration failed.
                            displayErrorSnackBar(task.exception!!.message.toString(), true)
                        }
                    })
        }
    }

    fun userRegisteredSuccessfully(){

        //Hide ProgressBar
        hideProgressBar()

        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
        startActivity(intent)
        Toast.makeText(this@RegisterActivity, resources.getString(R.string.registeredSuccessfully),Toast.LENGTH_SHORT).show()
    }





}