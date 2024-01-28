package com.example.myshopnetapp.Activities.Fragments.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myshopnetapp.Model.Category
import com.example.myshopnetapp.Model.Product
import com.example.myshopnetapp.Utilities.Resource
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class CategoryViewModel constructor(
    private val firestore: FirebaseFirestore,
    private val category: Category
): ViewModel() {

    private val _allProducts = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val allProducts = _allProducts.asStateFlow()

    //call the fun inside init block
    init {
        getAllProducts()
    }

    //the fun below will only get products that doesn't contain offerPercentage
    fun getAllProducts(){
        viewModelScope.launch {
            _allProducts.emit(Resource.Loading())
        }
        firestore.collection("Products")
            .whereEqualTo("category",category.category)
            .whereEqualTo("offerPercentage",null).get()
            .addOnSuccessListener {
                val products = it.toObjects(Product::class.java)
                viewModelScope.launch {
                    _allProducts.emit(Resource.Success(products))
                }
            }.addOnFailureListener{
                viewModelScope.launch {
                    _allProducts.emit(Resource.Error(it.message.toString()))
                }
            }
    }

}