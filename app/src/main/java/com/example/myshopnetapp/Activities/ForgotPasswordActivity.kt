package com.example.myshopnetapp.Activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.myshopnetapp.R
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : BaseActivity() {

    private lateinit var buttonSubmit: Button
    private lateinit var emailEditText: EditText
    private lateinit var textView_loginForgetPassword: TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        supportActionBar?.hide()


        textView_loginForgetPassword = findViewById(R.id.textView_loginForgetPassword)
        textView_loginForgetPassword.setOnClickListener {
            val intent = Intent(this@ForgotPasswordActivity, LoginActivity::class.java)
            startActivity(intent)
        }


        buttonSubmit = findViewById(R.id.button_submit)
        emailEditText = findViewById(R.id.editText_emailID)
        buttonSubmit.setOnClickListener {
            val emailID: String = emailEditText.text.toString().trim { it <= ' ' }
            if (emailID.isEmpty()) {
                displayErrorSnackBar(resources.getString(R.string.error_message_email), true)
            } else {
                displayProgressBar(resources.getString(R.string.pleaseWait))
                FirebaseAuth.getInstance().sendPasswordResetEmail(emailID)
                    .addOnCompleteListener { task ->
                        hideProgressBar()
                        if (task.isSuccessful) {
                            Toast.makeText(
                                this@ForgotPasswordActivity,
                                resources.getString(R.string.emailSuccessfullySent),
                                Toast.LENGTH_LONG
                            ).show()
                            finish()
                        } else {
                            displayErrorSnackBar(task.exception!!.message.toString(),true)
                        }
                    }
            }
        }
    }
}