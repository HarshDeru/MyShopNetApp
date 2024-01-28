package com.example.myshopnetapp.DatabaseFirestore

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import com.example.myshopnetapp.Activities.LoginActivity
import com.example.myshopnetapp.Activities.ProfileActivity
import com.example.myshopnetapp.Activities.RegisterActivity
import com.example.myshopnetapp.Activities.SettingsActivity
import com.example.myshopnetapp.Model.User
import com.example.myshopnetapp.Utilities.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class FirestoreClass {

    private val myFirestore = FirebaseFirestore.getInstance()

    //set function creates a user
    fun userRegister(activity: RegisterActivity, userInfo: User){

        //The name of collection is 'users', will not recreate the collection if its already existed.
        myFirestore.collection(Constants.USERS)
            //ID document for users field which is the users uniqueID.
            .document(userInfo.id)
            //the userInfor are user details field and setOption is to merge details instead of replacing it.
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {

                //call and runs the function from register activity
                activity.userRegisteredSuccessfully()
            }

        //if it fails then process...
            .addOnFailureListener{ e ->
                activity.hideProgressBar()
                Log.e(activity.javaClass.simpleName,"Error while registering the new user", e)

            }
    }

    fun getCurrentUserID(): String {
        //Instance of currentUser using FirebaseAuth
        val currentUser = FirebaseAuth.getInstance().currentUser

        //variable to assign the currentUserID if it is not null then store uid from authentication
        var currentUserID = ""
        if (currentUser != null){
            currentUserID = currentUser.uid
        }
        return currentUserID
    }

    //get fun gets the current user
    fun getCurrentUserDetails(activity: Activity){

        //get user data from users collection using constant.
        myFirestore.collection(Constants.USERS)
            //use document id to get field of user.
            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->

                Log.i(activity.javaClass.simpleName,document.toString())

                //Received the document that is converted into the user data model
                //store it in user val
                val user = document.toObject(User::class.java)!!

                //will be user to store shared preferences privately
                val sharedPreferences = activity.getSharedPreferences(
                    Constants.MYSHOPNET_PREFERENCES,
                    Context.MODE_PRIVATE)

                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                editor.putString(
                    Constants.LOGGED_USERNAME,
                    "${user.firstName} ${user.lastName}"
                )
                editor.apply()

                //start
                when (activity){
                    is LoginActivity -> {
                        //call the function from login activity
                        activity.userLoggedInSuccessfully(user)
                    }
                    is SettingsActivity -> {
                        //call the function from setting activity
                        activity.userProfileDetailsSuccess(user)

                    }
                }
                //END
            }

            .addOnFailureListener{ e ->
                //hide progress bar if an error is found
                when(activity){
                    is LoginActivity -> {
                        activity.hideProgressBar()
                    }
                    is SettingsActivity -> {
                        activity.hideProgressBar()
                    }
                }
                Log.e(activity.javaClass.simpleName,"Error while getting current user details",e)
            }
    }

    //updates the user details
    fun updateUserProfileDetails (activity: Activity,userHashMap : HashMap <String, Any>){
        myFirestore.collection(Constants.USERS)
            .document(getCurrentUserID())
            .update(userHashMap)
            .addOnSuccessListener {
                when (activity){
                    is ProfileActivity->{
                        activity.profileUpdatedSuccessfully()
                    }
                }

            }
            //hides the progress bar if an error occurred and prints out and error message.
            .addOnFailureListener{ e->
                when (activity){
                    is ProfileActivity->{
                        activity.hideProgressBar()
                    }
                }
                Log.e(
                    activity.javaClass.simpleName,"Error while updating the user details",e
                )
            }
    }

    //uploads the profile image to firebase storage
    fun uploadProfileImageToStorage (activity: Activity, imageFileURI: Uri?){
        //get storage reference (go to firebase storage)
        val sRef: StorageReference = FirebaseStorage.getInstance().reference.child(
            Constants.PROFILE_USER_IMAGE + System.currentTimeMillis() +"."
                    + Constants.getFileExtension(activity,imageFileURI)
        )
        sRef.putFile(imageFileURI!!).addOnSuccessListener { taskSnapshot ->
            //the image has successfully uploaded
            Log.e("Firebase ImageURL",taskSnapshot.metadata!!.reference!!.downloadUrl.toString())

            //get the downloadable url from the task snapshot
            //the uri in this case will be the link that will be stored on the firebase
            taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener { uri ->
                Log.e("Downloadable Image URL", uri.toString())
                //pass the uri (image URL) to another activity
                when (activity) {
                    is ProfileActivity ->{
                        activity.imageUploadedSuccessfully(uri.toString())
                    }
                }

            }
        }
                //hide the progress bas if an error occurs
            .addOnFailureListener{ exception ->
                when(activity){
                    is ProfileActivity -> {
                        activity.hideProgressBar()
                    }
                }
                Log.e(activity.javaClass.simpleName,exception.message, exception)
            }
    }







}