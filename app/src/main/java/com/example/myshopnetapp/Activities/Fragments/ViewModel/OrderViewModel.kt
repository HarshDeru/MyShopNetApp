package com.example.myshopnetapp.Activities.Fragments.ViewModel

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myshopnetapp.Model.Order
import com.example.myshopnetapp.Utilities.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import hilt_aggregated_deps._com_example_myshopnetapp_Activities_Fragments_ViewModel_AddressViewModel_HiltModules_KeyModule
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
): ViewModel() {

    private val _order = MutableStateFlow<Resource<Order>>(Resource.Unspecified())
    val order = _order.asStateFlow()

    fun placeOrder (order: Order) {
        viewModelScope.launch {
            _order.emit(Resource.Loading())
        }
        firestore.runBatch{ batch ->

            firestore.collection("users").document(auth.uid!!).collection("orders").document().set(order)

            firestore.collection("orders").document().set(order)

            firestore.collection("users").document(auth.uid!!).collection("cart").get()
                .addOnSuccessListener {
                    it.documents.forEach {
                        it.reference.delete()
                    }
                }
        }.addOnSuccessListener {
            viewModelScope.launch {
                _order.emit(Resource.Success(order))
            }
        }.addOnFailureListener {
            viewModelScope.launch {
                _order.emit(Resource.Error(it.message.toString()))
            }
        }
    }
}