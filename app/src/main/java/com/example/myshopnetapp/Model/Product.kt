package com.example.myshopnetapp.Model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Product(
    //unique identifier of the product
    val id: String,
    //name of the product
    val name: String,
    //category of the product
    val category: String,
    //price of the product
    val price: Float,
    //offer percentage for the product if any
    val offerPercentage: Float? = null,
    //description of the product if any
    val description: String? = null,
    //list of colors available for the product if any
    val colors: List<Int>? = null,
    //list of sizes available for the product if any
    val sizes: List<String>? = null,
    //list of image URLs for the product
    val images: List<String>
): Parcelable {
    //default constructor with empty values
    constructor(): this("", "","",0F,0F,"",listOf(),listOf(),listOf())
}