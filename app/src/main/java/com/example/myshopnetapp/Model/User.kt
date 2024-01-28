package com.example.myshopnetapp.Model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class User (
    //User properties
    //Allows the user's data to store in Firestore
    // Unique identifier for the user
    val id: String = "",
    // First name of the user
    val firstName: String = "",
    // Last name of the user
    val lastName: String = "",
    // Email address of the user
    val email: String = "",
    // Profile image URL of the user
    val image: String = "",
    // Mobile number of the user
    val mobile: Long = 0,
    // Gender of the user
    val gender: String = "",
    // Indicates the percentage of profile completion
    val profileCompleted: Int = 0
): Parcelable