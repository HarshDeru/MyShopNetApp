package com.example.myshopnetapp.Utilities

fun Float?.getProductPrice(price: Float): Float{
    // If the discount percentage is null, return the original price
    if(this == null)
        return price
    // Calculate the remaining price percentage after applying the discount
    val remainingPricePercentage = 1F - this
    // Calculate the price after applying the discount
    val priceAfterOffer = remainingPricePercentage * price
    // Return the price after applying the discount
    return priceAfterOffer
}