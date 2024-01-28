package com.example.myshopnetapp.Activities.ui.Cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myshopnetapp.DatabaseFirestore.FirestoreCart
import com.example.myshopnetapp.Model.CartProduct
import com.example.myshopnetapp.Utilities.Resource
import com.example.myshopnetapp.Utilities.getProductPrice
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

// Hilt dependency injection annotation
@HiltViewModel
class CartViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val firestoreCart: FirestoreCart
): ViewModel() {
    private val _cartProducts = MutableStateFlow<Resource<List<CartProduct>>>(Resource.Unspecified())
    val cartProducts = _cartProducts.asStateFlow()

    val productPrice = cartProducts.map {
        when(it){
            is Resource.Success ->{
                calculatePrice(it.data!!)
            }
            else -> null
        }
    }

    private val _deleteDialog = MutableSharedFlow<CartProduct>()
    val deleteDialog = _deleteDialog.asSharedFlow()

    private var cartProductDocument = emptyList<DocumentSnapshot>()

    fun deleteCartProducts(cartProduct: CartProduct){
        val index = cartProducts.value.data?.indexOf(cartProduct)
        if (index != null && index != -1) {
            val documentId = cartProductDocument[index].id
            firestore.collection("users").document(auth.uid!!).collection("cart").document(documentId).delete()
        }
    }

    private fun calculatePrice(data: List<CartProduct>): Float {
        return data.sumByDouble { cartProduct ->
            val quantity = cartProduct.quantity
            val price = cartProduct.product.price
            val offerPercentage = cartProduct.product.offerPercentage
            (offerPercentage.getProductPrice(price) * quantity).toDouble()
        }.toFloat()
    }

    // Fetch cart products from Firestore when the ViewModel is initialized
    init {
        getCartProducts()
    }

    // Function to fetch cart products from Firestore
    private fun getCartProducts() {
        viewModelScope.launch {
            _cartProducts.emit(Resource.Loading())
        }
        // Firestore collection reference to fetch cart products
        firestore.collection("users").document(auth.uid!!).collection("cart")
            .addSnapshotListener { value, error ->
                // If an error occurs or the value is null
                if (error != null || value == null) {
                    viewModelScope.launch {
                        _cartProducts.emit(Resource.Error(error?.message.toString()))
                    }
                }
                // If the value is not null and no error occurs
                else {
                    // Save document snapshots of cart products
                    // Convert the value to a list of cart products
                    cartProductDocument = value.documents
                    val cartProducts = value.toObjects(CartProduct::class.java)
                    viewModelScope.launch { _cartProducts.emit(Resource.Success(cartProducts))}
                }
            }
    }

    // Function to change quantity of a cart product
    fun changeQuantity(cartProduct: CartProduct,quantityChanging: FirestoreCart.QuantityChanging){
        // Get the index of the cart product to be updated
        val index = cartProducts.value.data?.indexOf(cartProduct)
        // If the cart product is found
        if (index != null && index != -1){
            // Get the ID of the cart product's Firestore document
            val documentId = cartProductDocument[index].id
            when (quantityChanging){
                // If the quantity is to be increased, call the function to update it
                FirestoreCart.QuantityChanging.INCREASE -> {
                    viewModelScope.launch { _cartProducts.emit(Resource.Loading()) }
                    increaseQuantity(documentId)
                }
                // If the quantity is to be decreased, call the function to update it
                FirestoreCart.QuantityChanging.DECREASE -> {
                    if(cartProduct.quantity == 1){
                        viewModelScope.launch { _deleteDialog.emit(cartProduct)}
                        return
                    }
                    viewModelScope.launch { _cartProducts.emit(Resource.Loading()) }
                    decreaseQuantity(documentId)
                }

            }
        }
    }

    // Function to increase the quantity of a cart product in Firestore
    private fun increaseQuantity(documentId: String) {
        // Call the FirestoreCart class to update the quantity
        firestoreCart.increaseQuantity(documentId){result,exception ->
            // If an exception occurs then display error
            if (exception !=null)
                viewModelScope.launch {
                    _cartProducts.emit(Resource.Error(exception.message.toString()))
                }
        }
    }

    // Function to decrease the quantity of a cart product in Firestore
    private fun decreaseQuantity(documentId: String) {
        // Call the FirestoreCart class to update the quantity
        firestoreCart.decreaseQuantity(documentId){result,exception ->
            // If an exception occurs then display error
            if (exception !=null)
                viewModelScope.launch {
                    _cartProducts.emit(Resource.Error(exception.message.toString()))
                }
        }
    }
}