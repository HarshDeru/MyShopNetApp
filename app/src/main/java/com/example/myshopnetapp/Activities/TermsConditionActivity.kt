package com.example.myshopnetapp.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.example.myshopnetapp.R
import kotlinx.android.synthetic.main.activity_terms_condition.*

class TermsConditionActivity : AppCompatActivity() {

    private lateinit var termsCondition_back_button: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms_condition)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        supportActionBar?.hide()

        termsCondition_back_button= findViewById(R.id.termsCondition_back_button)
        termsCondition_back_button.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

    }
}