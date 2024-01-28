package com.example.myshopnetapp.Activities.Fragments.Categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myshopnetapp.Adapters.BProductsAdapter
import com.example.myshopnetapp.R
import com.example.myshopnetapp.databinding.FragmentBaseCategoryBinding
import kotlinx.android.synthetic.main.fragment_base_category.*

open class BaseCategory: Fragment(R.layout.fragment_base_category){
    private lateinit var binding:FragmentBaseCategoryBinding
    protected val products: BProductsAdapter by lazy {BProductsAdapter()}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBaseCategoryBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupProducts()

        products.onClick = {
            val b = Bundle().apply { putParcelable("product",it) }
            findNavController().navigate(R.id.action_navigation_home_to_productDetailsFragment, b)
        }
    }

    fun displayProgressBar(){
        binding.baseCategoryProgressBar.visibility = View.VISIBLE
    }

    fun hideProgressBar(){
        binding.baseCategoryProgressBar.visibility = View.GONE
    }

    private fun setupProducts() {
        binding.recycleViewBaseProducts.apply{
            layoutManager = GridLayoutManager(requireContext(),2, GridLayoutManager.VERTICAL,false)
            adapter = products
        }
    }


}