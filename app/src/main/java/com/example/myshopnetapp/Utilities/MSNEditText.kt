package com.example.myshopnetapp.Utilities

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

class MSNEditText(context: Context, attrs: AttributeSet) : AppCompatEditText(context, attrs) {

    init {
        // Get the function to apply the font
        applyFont()
    }

    private fun applyFont(){

        //Get the file from assets folder and set the title name
        val typeface: Typeface = Typeface.createFromAsset(context.assets,"montserrat.regular.ttf")
        setTypeface(typeface)
    }
}