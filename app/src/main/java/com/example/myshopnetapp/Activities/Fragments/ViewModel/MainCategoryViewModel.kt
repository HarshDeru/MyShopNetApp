package com.example.myshopnetapp.Activities.Fragments.ViewModel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myshopnetapp.Model.Product
import com.example.myshopnetapp.Utilities.Resource
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainCategoryViewModel @Inject constructor(
    private val firestore: FirebaseFirestore
): ViewModel() {

    private val _specialProducts = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val sProducts:StateFlow<Resource<List<Product>>> = _specialProducts

    private val _bestDealProducts = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val bDealProducts:StateFlow<Resource<List<Product>>> = _bestDealProducts

    private val _bestProducts = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val bProducts:StateFlow<Resource<List<Product>>> = _bestProducts

    init{
        getSpecialProducts()
        getBestProduct()
        getBestDealProduct()

    }

    //the fun below is used to get the special product's detail stored within the firestore
    fun getSpecialProducts(){
        viewModelScope.launch {
            _specialProducts.emit(Resource.Loading())
        }
        firestore.collection("Products")
            .whereEqualTo("category","Special Products").get().addOnSuccessListener { result ->
                val specialProductList = result.toObjects(Product::class.java)
                viewModelScope.launch {
                    _specialProducts.emit(Resource.Success(specialProductList))
                }
            }
            .addOnFailureListener {
                viewModelScope.launch {
                    _specialProducts.emit(Resource.Error(it.message.toString()))
                }
            }
    }

    fun getBestDealProduct(){
        viewModelScope.launch {
            _bestDealProducts.emit(Resource.Loading())
        }
        firestore.collection("Products")
            .whereEqualTo("category","Best Offers").get().addOnSuccessListener { result ->
                val bestDealProductsList = result.toObjects(Product::class.java)
                viewModelScope.launch {
                    _bestDealProducts.emit(Resource.Success(bestDealProductsList))
                }
            }
            .addOnFailureListener {
                viewModelScope.launch {
                    _bestDealProducts.emit(Resource.Error(it.message.toString()))
                }
            }

    }

    fun getBestProduct(){
        viewModelScope.launch {
            _bestProducts.emit(Resource.Loading())
        }
        firestore.collection("Products")
            .whereEqualTo("category","Best Products").get().addOnSuccessListener { result ->
                val bestProductsList = result.toObjects(Product::class.java)
                viewModelScope.launch {
                    _bestProducts.emit(Resource.Success(bestProductsList))
                }
            }
            .addOnFailureListener {
                viewModelScope.launch {
                    _bestProducts.emit(Resource.Error(it.message.toString()))
                }
            }


    }

}