package com.example.myshopnetapp.Activities.Fragments.Frag

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myshopnetapp.Activities.Fragments.ViewModel.DetailsViewModel
import com.example.myshopnetapp.Adapters.ChooseSizeAdapter
import com.example.myshopnetapp.Adapters.viewPagerImages
import com.example.myshopnetapp.Model.CartProduct
import com.example.myshopnetapp.R
import com.example.myshopnetapp.Utilities.Resource
import com.example.myshopnetapp.databinding.FragmentProductDetailsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ProductDetailsFragment: Fragment() {
    private val args by navArgs<ProductDetailsFragmentArgs>()
    private lateinit var binding: FragmentProductDetailsBinding
    private val viewPagerAdapter by lazy { viewPagerImages()}
    private val sizeAdapter by lazy { ChooseSizeAdapter() }
    private var selectedSize: String? = null
    private val viewModel by viewModels<DetailsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductDetailsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val product = args.product

        setupProductImage()
        setupProductSize()

        //makes the user close the product view by clicking on cross/close icon
        binding.closeProductImage.setOnClickListener{
            findNavController().navigateUp()
        }

        sizeAdapter.onItemClick = {
            selectedSize = it
        }

        binding.buttonAddProductToCart.setOnClickListener{
            if (selectedSize != null) {
                viewModel.addUpdateProductInCart(CartProduct(product,1, selectedSize))
            } else {
                Toast.makeText(requireContext(),"Please select a size",Toast.LENGTH_SHORT).show()
            }
        }


        lifecycleScope.launchWhenStarted {
            viewModel.addToCart.collectLatest {
                when (it){
                    is Resource.Loading ->{
                        binding.buttonAddProductToCart.startAnimation()
                        }
                    is Resource.Success ->{
                        binding.buttonAddProductToCart.revertAnimation()
                        binding.buttonAddProductToCart.setBackgroundColor(resources.getColor(R.color.pinkThemeColor))
                        Toast.makeText(requireContext(),"Product Added Successful",Toast.LENGTH_SHORT).show()
                    }
                    is Resource.Error ->{
                        binding.buttonAddProductToCart.stopAnimation()
                        Toast.makeText(requireContext(),it.message,Toast.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }
        }

        binding.apply {
            textViewProductDetailsName.text = product.name
            textViewProductDetailsPrice.text = "£ ${product.price}"
            textViewProductDetailsDescriptions.text = product.description

            //makes the size text disappear if no sizes are added
            if (product.colors.isNullOrEmpty())
                textViewProductSizes.visibility = View.INVISIBLE
        }

        viewPagerAdapter.differ.submitList(product.images)
        product.sizes?.let { sizeAdapter.differ.submitList(it)}
    }

    private fun setupProductImage() {
        binding.apply {
            viewPager2Images.adapter = viewPagerAdapter
        }
    }

    private fun setupProductSize() {
        binding.recycleViewProductSize.apply {
            adapter = sizeAdapter
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        }
    }




}