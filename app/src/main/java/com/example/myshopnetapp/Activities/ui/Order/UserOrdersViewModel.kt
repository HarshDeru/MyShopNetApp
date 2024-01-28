package com.example.myshopnetapp.Activities.ui.Order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myshopnetapp.Model.Order
import com.example.myshopnetapp.Utilities.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserOrdersViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
): ViewModel() {

    private val _allOrder = MutableStateFlow<Resource<List<Order>>>(Resource.Unspecified())
    val allOrder = _allOrder.asStateFlow()

    init {
        getAllOrders()
    }

    fun getAllOrders(){
        viewModelScope.launch {
            _allOrder.emit(Resource.Loading())
        }

        firestore.collection("users").document(auth.uid!!).collection("orders").get()
            .addOnSuccessListener {
                val orders = it.toObjects(Order::class.java)
                viewModelScope.launch {
                    _allOrder.emit(Resource.Success(orders))
                }

            }.addOnFailureListener {
                viewModelScope.launch {
                    _allOrder.emit(Resource.Error(it.message.toString()))
                }

            }
    }
}