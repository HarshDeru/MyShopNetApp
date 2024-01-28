package com.example.myshopnetapp.Activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager.LayoutParams.*
import android.widget.TextView
import com.example.myshopnetapp.R


@Suppress("DEPRECATION")
class SplashActivity : AppCompatActivity() {

    private lateinit var tv_app_name: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        supportActionBar?.hide()


        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                FLAG_FULLSCREEN,
                FLAG_FULLSCREEN
            )
        }

        Handler().postDelayed(
            {
                // Launch Main Activity
                startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                finish() //Open this activity when SlashActivity is closed
            },
            2500
        )

        tv_app_name =findViewById(R.id.tv_app_name)

        //val typeface: Typeface = Typeface.createFromAsset(assets,"montserrat.bold.ttf")
        //tv_app_name.typeface = typeface

    }
}