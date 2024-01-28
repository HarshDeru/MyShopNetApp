package com.example.myshopnetapp.Activities.Fragments.Categories

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myshopnetapp.Activities.Fragments.ViewModel.MainCategoryViewModel
import com.example.myshopnetapp.Adapters.BDealProductAdapters
import com.example.myshopnetapp.Adapters.BProductsAdapter
import com.example.myshopnetapp.Adapters.SProductsAdapters
import com.example.myshopnetapp.Model.Product
import com.example.myshopnetapp.R
import com.example.myshopnetapp.Utilities.Resource
import com.example.myshopnetapp.databinding.FragmentHomeCategoryBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

private val TAG = "MainCategory"
@AndroidEntryPoint
class MainCategory: Fragment(R.layout.fragment_home_category) {
    private lateinit var binding: FragmentHomeCategoryBinding
    private lateinit var specialProductAdapter: SProductsAdapters
    private lateinit var bestDealProductAdapter: BDealProductAdapters
    private lateinit var bestProductAdapters: BProductsAdapter
    private val viewModel :  MainCategoryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeCategoryBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSpecialProducts()
        setupBestDealProducts()
        setupBestProducts()


        specialProductAdapter.onClick = {
            val b = Bundle().apply { putParcelable("product",it) }
            findNavController().navigate(R.id.action_navigation_home_to_productDetailsFragment, b)
        }

        bestProductAdapters.onClick = {
            val b = Bundle().apply { putParcelable("product",it) }
            findNavController().navigate(R.id.action_navigation_home_to_productDetailsFragment, b)
        }

        bestDealProductAdapter.onClick = {
            val b = Bundle().apply { putParcelable("product",it) }
            findNavController().navigate(R.id.action_navigation_home_to_productDetailsFragment, b)
        }

        lifecycleScope.launchWhenStarted {
             viewModel.sProducts.collectLatest {
                when (it){
                    is Resource.Loading ->{
                        displayProgressBar()
                    }
                    is Resource.Success ->{
                        specialProductAdapter.differ.submitList(it.data)
                        hideProgressBar()
                    }
                    is Resource.Error ->{
                        hideProgressBar()
                        Log.e(TAG,it.message.toString())
                        Toast.makeText(requireContext(),it.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.bDealProducts.collectLatest {
                when (it){
                    is Resource.Loading ->{
                        displayProgressBar()
                    }
                    is Resource.Success ->{
                        bestDealProductAdapter.differ.submitList(it.data)
                        hideProgressBar()
                    }
                    is Resource.Error ->{
                        hideProgressBar()
                        Log.e(TAG,it.message.toString())
                        Toast.makeText(requireContext(),it.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.bProducts.collectLatest {
                when (it){
                    is Resource.Loading ->{
                        displayProgressBar()
                    }
                    is Resource.Success ->{
                        bestProductAdapters.differ.submitList(it.data)
                        hideProgressBar()
                    }
                    is Resource.Error ->{
                        hideProgressBar()
                        Log.e(TAG,it.message.toString())
                        Toast.makeText(requireContext(),it.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }
        }


    }

    //hides the progress bar
    private fun hideProgressBar() {
        binding.homeCategoryProgressBar.visibility = View.GONE
    }

    //displays the progress bar
    private fun displayProgressBar() {
        binding.homeCategoryProgressBar.visibility = View.VISIBLE
    }

    //the code below will allow me to set up the special product as adapter is added and will be able to bind it with recycleview
    private fun setupSpecialProducts() {
        specialProductAdapter = SProductsAdapters()
        binding.recycleViewSpecialProducts.apply{
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false)
            adapter = specialProductAdapter
        }
    }

    private fun setupBestProducts() {
        bestProductAdapters = BProductsAdapter()
        binding.recycleViewBestProducts.apply{
            layoutManager = GridLayoutManager(requireContext(),2,GridLayoutManager.VERTICAL,false)
            adapter = bestProductAdapters
        }
    }

    private fun setupBestDealProducts() {
        bestDealProductAdapter = BDealProductAdapters()
        binding.recycleViewBestOffers.apply{
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false)
            adapter = bestDealProductAdapter
        }
    }



}