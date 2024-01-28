package com.example.myshopnetapp.Activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.example.myshopnetapp.Activities.Fragments.Frag.AddressFragment
import com.example.myshopnetapp.DatabaseFirestore.FirestoreClass
import com.example.myshopnetapp.Model.User
import com.example.myshopnetapp.R
import com.example.myshopnetapp.Utilities.Constants
import com.example.myshopnetapp.Utilities.GlideImageLoader
import com.example.myshopnetapp.Utilities.MSNTextView
import com.example.myshopnetapp.databinding.FragmentUserAddressBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_settings.*

@AndroidEntryPoint
class SettingsActivity : BaseActivity(), View.OnClickListener {

    private lateinit var sUserDetails: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        //OnClickListener for logout and edit button
        button_settingLogout.setOnClickListener(this)
        textView_settingEdit.setOnClickListener(this)

        val linearLayoutTrackOrder = findViewById<LinearLayout>(R.id.linearLayoutTrackOrder)

        linearLayoutTrackOrder.setOnClickListener {
            val snackbar = Snackbar.make(linearLayoutTrackOrder, "This feature is not available yet", Snackbar.LENGTH_SHORT)
            snackbar.show()
        }

        val linearLayoutAllOrders = findViewById<LinearLayout>(R.id.linearLayoutAllOrders)

        linearLayoutAllOrders.setOnClickListener {
            val intent = Intent(this, NavigationActivity::class.java)
            startActivity(intent)
        }





    }

    //gets the current user details from firebase database
    private fun getUserProfileDetails(){
        displayProgressBar(resources.getString(R.string.pleaseWait))
        FirestoreClass().getCurrentUserDetails(this)
    }

    //loads the profile details including images on the setting page
    fun userProfileDetailsSuccess(user:User){
        sUserDetails = user

        hideProgressBar()

        //loads all the data in specific text area
        GlideImageLoader(this@SettingsActivity).loadProfileImage(user.image, imageView_setting_userPhoto)
        textView_settingName.text = "${user.firstName} ${user.lastName}"

    }

    //resume the application so the app can save the last logged in user
    override fun onResume() {
        super.onResume()
        //gets the user details stored in firebase
        getUserProfileDetails()
    }

    //onClick listener for buttons
    override fun onClick(v: View?) {
        if (v !=null)
            when(v.id){
                //allows the user to access the profile page by clicking on the edit button
                R.id.textView_settingEdit ->{
                    val intent = Intent(this, ProfileActivity::class.java)
                    intent.putExtra(Constants.USER_DETAILS, sUserDetails)
                    startActivity(intent)
                }

                //allows the user to logout from the application
                R.id.button_settingLogout ->{
                    FirebaseAuth.getInstance().signOut()
                    val intent = Intent(this, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }
            }

    }


}