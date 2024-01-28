package com.example.myshopnetapp.Activities.Fragments.ViewModel.Factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myshopnetapp.Activities.Fragments.ViewModel.CategoryViewModel
import com.example.myshopnetapp.Model.Category
import com.google.firebase.firestore.FirebaseFirestore

class BaseCategoryViewModelFactory(
    private val firestore: FirebaseFirestore,
    private val category: Category
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CategoryViewModel(firestore,category)as T
    }
}