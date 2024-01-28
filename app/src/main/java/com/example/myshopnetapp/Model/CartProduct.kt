package com.example.myshopnetapp.Model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CartProduct(
    //A Product object that represents the product being added to the cart.
    val product: Product,
    //An Int that represents the quantity of the product being added to the cart.
    val quantity: Int,
    // An optional String that represents the size of the product being added to the cart.
    val selectedSize: String? = null
): Parcelable {
    //Initializes the properties with default values.
    constructor(): this(Product(),1,null)
}
