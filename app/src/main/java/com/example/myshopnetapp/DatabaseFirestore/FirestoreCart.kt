package com.example.myshopnetapp.DatabaseFirestore

import com.example.myshopnetapp.Model.CartProduct
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FirestoreCart(
    // instance of FirebaseFirestore and FirebaseAuth class
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
    ){

    // Accessing the cart collection for the current user
    private val cartCollection = firestore.collection("users").document(auth.uid!!).collection("cart")

    // Adding a product to the cart
    fun addProductToCart(cartProduct: CartProduct, onResult: (CartProduct?, Exception?) -> Unit){
        // set the product details in the Firestore document
        cartCollection.document().set(cartProduct)
            .addOnSuccessListener {
                // return the success result
            onResult(cartProduct,null)
        }.addOnFailureListener {
                // return the success result
            onResult(null,it)
        }
    }

    // Increasing the quantity of a product in the cart
    fun increaseQuantity(documentId:String, onResult: (String?, Exception?) -> Unit){
        firestore.runTransaction { transition ->
            // get the document reference from the cart collection
            val documentRef = cartCollection.document(documentId)
            val document = transition.get(documentRef)
            // convert the document data to CartProduct class object
            val productObject = document.toObject(CartProduct::class.java)
            // check if the CartProduct object is not null
            productObject?.let { cartProduct ->
                // increase the product quantity by 1
                val newQuantity = cartProduct.quantity + 1
                // create a new CartProduct object with updated quantity
                val newProductObject = cartProduct.copy(quantity = newQuantity)
                transition.set(documentRef,newProductObject)
            }
        }.addOnSuccessListener {
            // return the success result
            onResult(documentId,null)
        }.addOnFailureListener {
            // return the failure result
            onResult(null,it)
        }
    }

    // Decreasing the quantity of a product in the cart
    fun decreaseQuantity(documentId:String, onResult: (String?, Exception?) -> Unit){
        firestore.runTransaction { transition ->
            // get the document reference from the cart collection
            val documentRef = cartCollection.document(documentId)
            val document = transition.get(documentRef)
            // convert the document data to CartProduct class object
            val productObject = document.toObject(CartProduct::class.java)
            // check if the CartProduct object is not null
            productObject?.let { cartProduct ->
                // decrease the product quantity by 1
                val newQuantity = cartProduct.quantity - 1
                // create a new CartProduct object with updated quantity
                val newProductObject = cartProduct.copy(quantity = newQuantity)
                transition.set(documentRef,newProductObject)
            }
        }.addOnSuccessListener {
            // return the success result
            onResult(documentId,null)
        }.addOnFailureListener {
            // return the failure result
            onResult(null,it)
        }
    }

    // define an enum for quantity changing so I can use these constants whenever I want
    enum class QuantityChanging{
        INCREASE,DECREASE
    }


}