package com.example.myshopnetapp.Activities.Fragments.Categories

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.myshopnetapp.Activities.Fragments.ViewModel.CategoryViewModel
import com.example.myshopnetapp.Activities.Fragments.ViewModel.Factory.BaseCategoryViewModelFactory
import com.example.myshopnetapp.Model.Category
import com.example.myshopnetapp.Utilities.Resource
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class TShirtFragment: BaseCategory() {
    @Inject
    lateinit var firestore: FirebaseFirestore

    val viewModel by viewModels<CategoryViewModel>{
        BaseCategoryViewModelFactory(firestore, Category.TShirts)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenStarted {
            viewModel.allProducts.collectLatest {
                when (it){
                    is Resource.Loading -> {
                        displayProgressBar()

                    }
                    is Resource.Success -> {
                        products.differ.submitList(it.data)
                        hideProgressBar()
                    }
                    is Resource.Error -> {
                        Snackbar.make(requireView(),it.message.toString(), Snackbar.LENGTH_LONG).show()
                        hideProgressBar()
                    }
                    else -> Unit
                }
            }
        }




    }
}