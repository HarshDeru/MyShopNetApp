package com.example.myshopnetapp.Model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Address(
    val fullName: String,
    val houseNumber: String,
    val street: String,
    val mobileNumber: String,
    val city: String,
    val postCode: String
): Parcelable {
    constructor():this("","","","","","")
}